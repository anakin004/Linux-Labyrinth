package com.example.demo.model;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "api_keys")
public class ApiEntity{
 


     private static class ApiKeyGenerator {

        public static String generateApiKey() {
            return UUID.randomUUID().toString();
        }
     }
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private int remaining_uses;
	
    @Column(nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(unique = true, nullable = false)
    @NotNull
    private String apiKey;
    
    private ApiEntity(){
    }	

    public ApiEntity(String username){
        remaining_uses = 100;
        apiKey = ""; // empty intially
        this.username = username;
    }
    
    public String makeApiKey(){
	apiKey = ApiKeyGenerator.generateApiKey();
	return apiKey; // for user to save
    }
	
    public String getName(){
	return username;
    }	
	
    public int getUses(){
	return remaining_uses;	
    }


}





