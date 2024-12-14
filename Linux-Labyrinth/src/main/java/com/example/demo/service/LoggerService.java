package com.example.demo.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class LoggerService {
     public static void Log(String username, String command, String path){
           try(FileWriter writer = new FileWriter(path, true)){
           writer.write(username + ": " + command + " at, " + LocalTime.now().toString() + "\n");
	   }
	   catch (Exception e){
           e.printStackTrace();
           }
     }
}
