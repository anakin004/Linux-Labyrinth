package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import org.springframework.security.core.context.SecurityContextHolder;



@Service
public class GetUserService {

    private final PlayerRepository playerRepository;

    @Autowired
    public GetUserService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public String getPlayerPath(String username){
        return playerRepository.findByUsername(username).getPath();
    }

    public String getPlayerName(){
        return SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
    }

    public String getPlayerAnswers(String username){

        PlayerEntity p = playerRepository.findByUsername(username);
        StringBuilder sb = new StringBuilder("");

        sb.append(p.getAnswer1() + " ");
        sb.append(p.getAnswer2() + " ");
        sb.append(p.getAnswer3() + " ");
        sb.append(p.getAnswer4() + " ");
        sb.append(p.getAnswer5() + " ");
        sb.append(p.getAnswer6() + " ");
        sb.append(p.getAnswer7());

        return sb.toString();
    }


}