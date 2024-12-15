package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.PlayerEntity;
import com.example.demo.model.PlayerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.demo.model.ApiRepository;
import com.example.demo.model.ApiEntity;


@Service
public class GetUserService {
    
    private final AuthenticationService authService;
    private final PlayerRepository playerRepository;
    private final ApiRepository apiRepository;
    @Autowired
    public GetUserService(AuthenticationService authService, ApiRepository apiRepository, PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.apiRepository = apiRepository;
        this.authService = authService;
    }


    public String getPlayerPath(String username){
        return playerRepository.findByUsername(username).getPath();
    }

    public String getPlayerName(){
        return SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
    }

    public boolean getMadeApiKeyStatus(String username){
	PlayerEntity p = playerRepository.findByUsername(username); 
        return p.getMadeApi();

    }

    public String getUsernameFromApi(String apiKey) throws Exception{
	ApiEntity ae = apiRepository.findByApiKey(apiKey);
	if(ae == null)
	     throw new Exception("Invalid Api Key");
	if(ae.getUses() == 0)
	     throw new Exception("No more API-Uses Left");
	return ae.getName();
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
