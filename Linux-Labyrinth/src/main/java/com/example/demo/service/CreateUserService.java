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
    private final GetUserService getService;	
    private final ApiRepository apiRepository;

    @Autowired
    public CreateUserService(ApiRepository apiRepository, GetUserService getService, PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.getService = getService;
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

	if(username.contains(" "))
	    success = false;
	else if(username.matches(".*[^a-zA-Z0-9].*"))
	    success = false;
        else if( !checkUserExist(username) ){
            PlayerEntity player = new PlayerEntity(username, passwordEncoder.encode(password));
	    playerRepository.save(player);
            success = true;
        }       
        
        return success;
    }

     // the only way to create api key is if your successfully logged in 
     public String createApiKey(String username){
          String ret = "Already Generated Api Key Sorry!";
	
          if( !getService.getMadeApiKeyStatus(username) ){
	       ApiEntity ae = new ApiEntity(username);
	       ret = ae.makeApiKey();
               apiRepository.save(ae);
	       playerRepository.updateMadeApiStatusForUser(username, true);
          }

	return ret;	  
	          
     }



}


