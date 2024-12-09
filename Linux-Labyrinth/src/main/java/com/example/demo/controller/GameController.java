package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.demo.model.PlayerRepository;
import com.example.demo.model.PlayerEntity;
import org.springframework.ui.Model;

// for shell code stuff
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


import java.util.Arrays;
import java.util.List;

@RestController
public class GameController {


    
    private final PlayerRepository playerRepository;

    @Autowired
    public GameController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }




     // a request body class to handle the command
     // using @RequestBody
     // the user input will be put into a CommandRequest object 
     // we make it private static since it is associated with the class not with the class instance, and is only used in this class
     // so we can make CommandRequest objects without instances of the GameController class
    private static class CommandRequest {
        private String command;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }



    /*

    Executed shell command via processBuilder, reads into and InputStreamReader and BufferedReader to return result to user
    Alot of the checking for valid commands happens before
    this simply runs it
    
    Except ... 
    we do check the filePath entity if it is specifid
    it is mean for commands like cat so far, if the file is within the current directory
    cat does not support backwards travel, so we only check if its a subfile

    */
    public String executeShellCommand(String command, PlayerEntity p, String filePath) throws Exception {
        
        // case when we are doing cat, we need to check if its a valid file path
        if( filePath != null ){
            if( !isSubFile(p.getPath(),filePath) ){
                throw new Exception("Invalid File Path");
            }
        }

        String dir = p.getPath();
        // we know this dirFile is valid since the path is checking before sql input
        File dirFile = new File(dir);

        // create  ProcessBuilder to execute the shell command
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.directory(dirFile);  // setting the working directory for the command

        Process process = processBuilder.start();

        //  BufferedReader reads output InputStreamReader stream
        // InputStreamReader converts the byte stream from the process output stream to a character stream

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // if I had a quarter for every time I wrote stream in this function I would be able to buy
        // a McChicken

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // Wait for the process to finish and get its exit code
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new Exception("Error executing command, exit code: " + exitCode);
        }


        return output.toString();
    }



    // checks if any parameters specificed in the input command have backwards travel
    // will need for final, cat, and ls commands to name a few
    // if we are, return true, else return false
    public static boolean checkBackwardsCall(String[] params){
        for (String param : params) {
            if (param.contains("..")) {
                return true;
            }
        }

        return false;

    }




    // checks if any parameters use the absolute path
    // we can simply check if the first character of a param is a "/"
    // if it is then we know it starts an absolute path
    // then return true, else false
    public static boolean checkAbsPath(String[] params){

        for( String s: params){
            if( s.charAt(0) == '/') {
                return true;
            }
        }
        return false;
    }





    // print wrapper 
    public static <T> void print(T s){
        System.out.println(s);
    }




    public static boolean isSubdirectory(String child) {
        Path parentPath = Paths.get("").toAbsolutePath().getParent().resolve("labyrinth").normalize();
        Path childPath = Paths.get(child).toAbsolutePath().normalize();

        // check if both paths exist and are directories
        if (!Files.isDirectory(parentPath) || !Files.isDirectory(childPath)) {
            return false; 
        }
      
        // check if childPath starts with parent path
        return (childPath.startsWith(parentPath) || parentPath.equals(childPath));
    }




    // isSub file takes in the players current directory, and a "child" path
    // the input child path will need to include the absolute path for generality
    public static boolean isSubFile(String parent, String child) {
        
        Path childPath = Paths.get(child);
        Path parentPath = Paths.get(parent);

        // check if both paths exist and are directories
        return (Files.isDirectory(parentPath)    && 
                childPath.startsWith(parentPath) &&
                Files.isRegularFile(childPath)          
                );
    }

    // changeDir handles error checking
    // input dir is the going to be the full current player directory path + dest dir, so if I am in dir1
    // dir1 ---
    //       |
    //       -- file1
    //       -- dir2
    //       -- dir3
    // 
    // input to this function is ..../dir1 + / + dir
    //
    // this function also works for relative path changing like
    // if i was in dir3 i can do cd ../dir2
    public void changeDir(PlayerEntity p, String dir) throws Exception{

        boolean success = true;

        if( !isSubdirectory(dir) ){
            success = false;
        }


        else{
            // get rid of ../ and ..'s
            dir = Paths.get(dir).normalize().toString();
            // else we are going to update column with the new path
            // then return the success of the update
            success = playerRepository.updateColumnForUser(p.getName(), dir) == 1;
        }

        if( !success ){
            throw new Exception("Invalid Path");
        }
    }   


    // concats an arr of strings from beg to end
    public static String concat(String[] params, int beg, int end){
        StringBuilder sb = new StringBuilder("");

        for(int i = beg; i < end; i++){
            sb.append(params[i] + " ");
        }

        return sb.toString();
    }



    // The input.matches("[a-zA-Z0-9._\\-/\\s]+") ensures that the string contains only:
    // - a-z: lowercase alphabetic characters
    // - A-Z: uppercase alphabetic characters
    // - 0-9: numeric characters
    // - . : period character
    // - _ : underscore character
    // - \\: backslash character 
    // - /: forward slash character
    // - - : hyphen character
    // - \\s: any whitespace character (spaces, tabs, line breaks)
    // The '+' at the end means that the input must contain at least one or more of these allowed characters.
    // even though in checkCommand, we check first if its length of 0, then we rent
    // so adding the '+' here is slightly redundant, but for generality, its better to keep it

    public static boolean isValidInput(String input) {

        // edge case, we only want to allow flags if it is a find and uses -name
        // we will only allow flags for find, further checking happens in the containsBlackListed function below
        String[] params = input.split(" ");
        if( params.length >= 1 && params[0].equals("find") ){
            return input.matches("[a-zA-Z0-9._\\-/\\s]+");
        }

        return input != null && input.matches("[a-zA-Z0-9._\\/\\s]+");
    }

    // cant add and remove in the blackListedFlags
    private static final List<String> blackListedFlags = Arrays.asList(
        "-rf", "-f", "-r", "-exec", "-delete", "-v", "-i", "> /dev/null", "2>&1", "&&", "||", ";",
        "--help", "-a", "--no-preserve-root", "-S", "--remove", "-c", "--recursive", "-o", "-L", 
        "--force", "-T", "--unlink", "--strip", "--all", "--no-preserve-root"
    );

    // checks blacklisted flags in our input
    public static boolean containsBlackListed(String command) {
        for (String flag : blackListedFlags) {
            if (command.contains(flag)) {
                return true;
            }
        }
        return false;
    }


    

    // checks command and runs the command
    /*

    So far I am registering these commands:

        - cd  <dir>, changes current working dir, supports relative paths, upward travel - ../ , ../.., etc, no upward travel from home dir
        - find <multiple params>, used to find a input file usually using -name
        - ls <dir> prints contents of dir, if not dir, then contents of current dir
        - cat : prints file contents
        - help ( not the linux help command but for introduction )
        - echo : prints
        - tree  <dir> : prints directory structure in nice format, dir is optional
        - python3 <filename>.py : runs python file


    */
    public String checkAndRunCommand(String command) throws Exception{

        if ( command.length() == 0 ) { return ""; }
        if ( !isValidInput(command) ){ return "Invalid Command"; }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlayerEntity player = playerRepository.findByUsername(username);
        String[] params = command.split(" ");

        Path cwd = Paths.get("").toAbsolutePath().getParent().resolve("labyrinth").normalize();
        String playerPath = player.getPath();


        // checking if the path entered is absolute path
        if( checkAbsPath(params) ){
            return "Please don't use the absolute path in parameters with a forward slash at the beginning!";
        }

        if( containsBlackListed(command ) ){
            return "Contains blacklisted flag, please do not use it!";
        }

        print(command);
        // we will need this for cat, ls, find, ...
        // we will check specifically in cd and handle seperatly
        // but in cat, ls, find, I decided to just disable it
        boolean goesBackwards = checkBackwardsCall(params);


        switch(params[0]){




            // not the linux help command but is simply to be more info about game
            case "help":
                {

                    return String.format("Welcome! %s\n" +
                        "This marks the start of your journey!\n" +
                        " ---\n" +
                        "-+ quick side note -+\n" +
                        "help is a Linux command but it is used for introduction\n" +
                        " ---\n" +
                        "You can quit at any time and your progress will be saved\n" +
                        "To begin your journey type ls in the command prompt\n" +
                        "-------------------------------------------------------\n" +
                        "You will need to find a command that looks like command clue(x).txt\n" +
                        "Hint: command is all lowercase, and is the name of a domestic feline related to lions\n" +
                        "this command will be essential in seeing what all the clues hold!\n" +
                        "there are seven total keywords needed to solve the puzzle!" +
                        "When you fine all seven keywords, you can run answer.py, more on that later ...\n" +
                        "-------------------------------------------------------\n" +
                        "Enjoy!", player.getName());

                }





            // change directory command
            case "cd":
                {
                        
                    // case when its cd " " - being cd nothing, we go back to the labyrinth dir
                    if( params.length == 1 || (params.length == 2 && params[1].equals("~")) ){
                        changeDir(player, cwd.toString());
                    }   

                    // too many args just return
                    else if( params.length > 2 ){
                        return "Too many arguments!";
                    }

                    // from this point on any other if statement executed with be params.length  == 2

                    // go to parent dir, bound checking happens in changedir
                    else if( params[1].equals("..") ){
                        changeDir(player, Paths.get(playerPath).getParent().toString());
                    }

                    else {
                        
                        // if the command starts with a / we dont want to pad it with an additional / 

                        changeDir(player, playerPath + "/" + params[1] );
                    }

                    return "";

                }
                        




            // list dir
            case "ls":     // passing null since we arennt passing an additional file for this implimentation
                {
                    
                    String ret = "Too many arguments";

                    // checking backwards ls
                    if( goesBackwards ){
                        ret = "Backwards disabled for ls, sorry!";
                    }

                    else if( params.length <= 2)
                        ret = executeShellCommand(command, player, null);
       
                    return ret;
                }




            // cat -> prints file contents 
            case "cat":

                {

                    String ret = "Can't find file!";

                    // edge case, we don't want to let the user be able to print out the contents of answer.py
                    if(params[1].equals("answer.py") || params[1].equals("run.py") ){
                        ret = "Sorry! You can't read python files =(";
                    }

                    // checking backwards ls
                    else if( goesBackwards ){
                        ret = "Backwards disabled for cat, sorry!";
                    }

                    else{
                        // building response since we can cat multiple files
                        StringBuilder sb = new StringBuilder("");
                        for( int i = 1; i < params.length; i++){
                            sb.append(executeShellCommand("cat "+ params[i], player, null));
                        }

                        ret = sb.toString();
                    }

                    return ret;

                }



            
            case "echo":
                {   
                    // even thought we can chain commands when we enter an uneven ammount of quotes
                    // I decide for the scope of the project for now that we just replace it in echo
                    String res = "";
                    if( params.length > 1){

                        if( params[1].equals("linux") ){
                            res = params[1].replace("\"", "") + "\nCorrect! " +
                                                                "Naviagte to the root directory, and run run.py\n" +
                                                                "To run python files you will type python3 then filename\n" +
                                                                "specifically:\n"+
                                                                "python3 <filename>.py\n";
                        }
                        else{
                            res = concat(params, 1, params.length);
                        }
                    }
                    return res;

                }






            case "find":
                {
                    
                    String ret = "Couldn't find path!";

                    // there are many ways you can do a find command
                    // for this project we only want to search by name

                    // this accounts for any backwards finding
                    if( goesBackwards ){
                        ret = "Backwards disabled for find, sorry!";
                    }

                    else if( params.length <= 3 ){
                        ret = executeShellCommand( command, player, null);
                    }

                    else if( params.length == 4 ){
                        params[3] = params[3].replace("\"","");
                        ret = executeShellCommand( command , player, null );
                    }

                    
                    return ret;
                }

            



            case "tree":
                {

                    String ret = "Invalid Command";

                    if( goesBackwards ){
                        ret = "Backwards disabled for tree, sorry!";
                    }

                    else if( params.length <= 2 ){
                        ret = executeShellCommand( command, player, null );
                    }

                    return ret;

                }


            case "python3":

                {
                    String ret = "Coudln't find file!, make sure the filename is <filename>.py";

                    if( goesBackwards ){
                        ret = "Can't go backwards for python executables, sorry!";
                    }

                    else if( params.length == 2 ){
                        ret = executeShellCommand( command, player, null);
                    }

                    return ret;
                }

            default:
                throw new Exception("Invalid Command");


        }

    }

    public String jsonify(String ret) {
        // Replace newline characters with the string \\n
        String escapedRet = ret.replace("\n", "\\n");
        return String.format("{\"message\": \"%s\"}", escapedRet);
    }
    
    @PostMapping("/api/execute-command")
    public ResponseEntity<String> executeCommand(@RequestBody CommandRequest commandReq) {
        try {

            // getting the command from the request body
            String command = commandReq.getCommand();
            String ret = checkAndRunCommand(command);

            return ResponseEntity.ok(jsonify(ret));

        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity
                .badRequest()
                .body("Error executing command: " + e.getMessage());
        }
    }
}
