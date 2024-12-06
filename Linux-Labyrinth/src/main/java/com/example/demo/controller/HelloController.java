package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import org.springframework.ui.Model;

@RestController
public class HelloController {
    
    private final PlayerRepository playerRepository;

    @Autowired
    public HelloController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    
    @GetMapping("/hello")
    public String sayHello() {
        return "Hi, Spring Boot from VS Code!";
    }


    @GetMapping("/hellosql")
    public String saySQL() {

        PlayerEntity player = playerRepository.findByUsername("anakin");
        
        String answer1 = player.getAnswer1();

        return answer1;
    }
}
