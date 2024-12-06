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



    public String executeShellCommand(String command, String dir) throws Exception {
        // Use the provided `dir`, or default to "labyrinth/" if `dir` is null or empty
        if (dir == null || dir.isEmpty()) {
            dir = "../labyrinth/";  // Default directory
        }

        // Ensure the directory exists and is a directory
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            throw new Exception("Directory does not exist: " + dir);
        }

        // Create the ProcessBuilder to execute the shell command
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.directory(dirFile);  // Set the working directory for the command

        Process process = processBuilder.start();

        // reading the output from the command
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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






    public String checkCommand(String command) throws Exception{

        if (command.length() == 0){ return ""; }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlayerEntity player = playerRepository.findByUsername(username);

        String[] params = command.split(" ");
        switch(params[0]){

            case "cd":
                return "";
            
            case "ls":
                String ret = executeShellCommand("ls", "");
                System.out.println(ret);
                // temp dir 
                return executeShellCommand("ls", "");

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
            e.printStackTrace();
            return ResponseEntity
                .badRequest()
                .body("Error executing command: " + e.getMessage());
        }
    }
}
