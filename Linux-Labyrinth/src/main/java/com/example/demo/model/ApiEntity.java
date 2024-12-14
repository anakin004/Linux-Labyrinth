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
	    BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            return bc.encode(UUID.randomUUID().toString());

        }
     }
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private int remaining_uses;

    @Column(unique = true, nullable = false)
    @NotNull
    private String apiKey;

    public ApiEntity(){
        remaining_uses = 100;
        apiKey = ApiKeyGenerator.generateApiKey();
    }

}





