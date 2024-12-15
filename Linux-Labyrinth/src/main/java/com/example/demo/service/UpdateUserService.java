package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import com.example.demo.model.ApiRepository;
import com.example.demo.model.ApiEntity;


@Service
public class UpdateUserService {

    private final PlayerRepository playerRepository;
    private final ApiRepository apiRepository;

    @Autowired
    public UpdateUserService(ApiRepository apiRepository, PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
	this.apiRepository = apiRepository;
    }

    public void updateApiUseage(String apiKey){
	apiRepository.reduceRemainingCountByApiKey(apiKey);	
    }

    public boolean updateColumn(String name, String column, String value){

        boolean success = true;

        switch( column ){

            case "answer_1":
                playerRepository.updateAnswer1ForUser(name, value);
                break;

            case "answer_2":
                playerRepository.updateAnswer2ForUser(name, value);
                break;

            case "answer_3":
                playerRepository.updateAnswer3ForUser(name, value);
                break;

            case "answer_4":
                playerRepository.updateAnswer4ForUser(name, value);
                break;

            case "answer_5":
                playerRepository.updateAnswer5ForUser(name, value);
                break;

            case "answer_6":
                playerRepository.updateAnswer6ForUser(name, value);
                break;

            case "answer_7":
                playerRepository.updateAnswer7ForUser(name, value);
                break;

            case "currentpath":
                playerRepository.updatePathForUser(name, value);
                break;
            
            default:
                success = false;
                break;

        }

        return success;

    }

	

}
