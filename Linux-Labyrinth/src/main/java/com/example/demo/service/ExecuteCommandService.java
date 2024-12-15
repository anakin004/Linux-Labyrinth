package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import com.example.demo.service.GetUserService;
import com.example.demo.model.ApiEntity;
import com.example.demo.model.ApiRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExecuteCommandService {

    private final PlayerRepository playerRepository;
    private final GetUserService getService;
    private final ApiRepository apiRepository;	

    @Autowired
    public ExecuteCommandService(ApiRepository apiRepository, PlayerRepository playerRepository, GetUserService getService) {
        this.playerRepository = playerRepository;
        this.getService = getService;
	this.apiRepository = apiRepository;
    }

    /*

    Executed shell command via processBuilder, reads into and InputStreamReader and BufferedReader to return result to user
    Alot of the checking for valid commands happens before
    this simply runs it

    */

    public String executeShellCommand(String command, String username) throws Exception {
        
        String playerPath = getService.getPlayerPath(username);

        // we know this dirFile is valid since the path is checking before sql input
        File dirFile = new File(playerPath);

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

    /*

    also executes shell command via processBuilder, reads into and InputStreamReader and BufferedReader to return result to user
    specfically for python files, since we want to sometimes attach input like command line arguments

    */
    public String executePythonFile(String pathToScript, String username, String input) throws Exception {
        
        String playerPath = getService.getPlayerPath(username);

        // we know this dirFile is valid since the path is checking before sql input
        File dirFile = new File(playerPath);


        // Create list of command arguments
        List<String> command = new ArrayList<>();
        command.add("python3");
        command.add(pathToScript);
        
        // adding input as argument if provided
        if (input != null) {
            command.add(input);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);


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




}
