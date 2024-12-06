package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;


@Service
public class CreateUserService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateUserService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean checkUserExist(String username){

        if( playerRepository == null){
                System.out.println("playerepo null");
                return false;
        }

        boolean found = false;
        PlayerEntity player = playerRepository.findByUsername(username);

        if( player != null ){
            found = true;
        }

        return found;
    }


    public boolean create(String username, String password){

        boolean success = false;

        if( !checkUserExist(username) ){
            PlayerEntity player = new PlayerEntity(username, passwordEncoder.encode(password));
            playerRepository.save(player);
            success = true;
            System.out.println("New player");
        }       
        
        return success;
    }





}