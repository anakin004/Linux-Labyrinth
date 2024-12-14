package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import com.example.demo.model.ApiEntity;
import com.example.demo.model.ApiRepository;

@Service
public class CreateUserService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApiRepository apiRepository;  

    @Autowired
    public CreateUserService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, ApiRepository apiRepository) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
	this.apiRepository = apiRepository;
    }

    private boolean checkUserExist(String username){
        
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
            ApiEntity api = new ApiEntity();
	    playerRepository.save(player);
	    apiRepository.save(api);
            success = true;
        }       
        
        return success;
    }





}


