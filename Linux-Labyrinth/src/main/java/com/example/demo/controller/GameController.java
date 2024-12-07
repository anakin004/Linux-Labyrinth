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

@RestController
public class GameController {
    
    private final PlayerRepository playerRepository;

    @Autowired
    public GameController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


     // a request body class to handle the command
    public static class CommandRequest {
        private String command;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }


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

        System.out.println(dir);
        System.out.println(filePath);
        System.out.println(command);

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
        System.out.println(output);
        return output.toString();
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

    public static boolean isSubFile(String playerPath, String child) {
        
        Path childPath = Paths.get(child).toAbsolutePath().normalize();
        Path parentPath = Paths.get(playerPath);

        // check if both paths exist and are directories
        return (Files.isDirectory(parentPath)    && 
                childPath.startsWith(parentPath) &&
                Files.isRegularFile(childPath)          
                );
    }

    // changeDir handles error checking
    public void changeDir(PlayerEntity p, String dir) throws Exception{

        boolean success = true;

        if( !isSubdirectory(dir) ){
            success = false;
        }

        else{
            // else we are going to update column with the new path
            // then return the success of the update
            success = playerRepository.updateColumnForUser(p.getName(), dir) == 1;
        }

        if( !success ){
            throw new Exception("Invalid Path");
        }
    }   

    


    public String checkCommand(String command) throws Exception{

        if (command.length() == 0){ return ""; }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlayerEntity player = playerRepository.findByUsername(username);
        String[] params = command.split(" ");

        Path cwd = Paths.get("").toAbsolutePath().getParent().resolve("labyrinth").normalize();
        String playerPath = player.getPath();


        switch(params[0]){



            case "help":
                return String.format("Welcome! %s\n" +
                     "This marks the start of your journey!\n" +
                     "You can quit at any time and your progress will be saved\n" +
                     "To begin your journey type ls in the command prompt\n" +
                     "-------------------------------------------------------\n" +
                     "You will need to find a command that looks like command clue(x).txt\n" +
                     "Hint: command is all lowercase, and is the name of a domestic feline related to lions\n" +
                     "-------------------------------------------------------\n" +
                     "Enjoy!", player.getName());


            // change directory command
            case "cd":

                // case when its cd " " - being cd nothing, we go back to the labyrinth dir
                if( params.length == 1 ){
                    changeDir(player, cwd.toString());
                }   

                // go to parent dir, bound checking happens in changedir
                else if( params[1].equals("..") ){
                    changeDir(player, Paths.get(playerPath).getParent().toString());
                }

                else {
                    changeDir(player, cwd.toString() + "/" + params[1] );
                }

                return "";
            

            // list dir
            case "ls":     // passing null since we arennt passing an additional file for this implimentation
                return executeShellCommand("ls", player, null);



            case "cat":
                StringBuilder sb = new StringBuilder("");
                for( int i = 1; i < params.length; i++){
                    sb.append(executeShellCommand("cat "+ params[i], player, playerPath + "/" + params[i]));
                }
                return sb.toString();
            

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
            String ret = checkCommand(command);

            return ResponseEntity.ok(jsonify(ret));

        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity
                .badRequest()
                .body("Error executing command: " + e.getMessage());
        }
    }
}
