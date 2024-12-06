package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.PlayerRepository;
import org.springframework.ui.Model;

@RestController
public class GameController {
    
    private final PlayerRepository playerRepository;

    @Autowired
    public GameController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    
    @GetMapping("/api/execute-command")
    public ResponseEntity<String> executeCommand(){
        // placeholder
        String result = "Command executed successfully!";
        
        // Return a response with status code 200 (OK)
        return ResponseEntity.ok(result);
    }
}
