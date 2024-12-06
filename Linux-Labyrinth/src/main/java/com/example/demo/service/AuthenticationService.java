package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import org.springframework.context.annotation.Bean;

/* 
this service will interact with the PlayerRepository 
by comparing password to the input user if one is found
*/

@Service
public class AuthenticationService {


    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // auth the user by checking their password
    public boolean authenticate(String username, String password) {
        PlayerEntity player = playerRepository.findByUsername(username);
        if (player != null) {
            return passwordEncoder.matches(passwordEncoder.encode(password), player.getPassword());
        }
        return false;

    }

}