package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration  // marks this as a configuration class
public class BeanConfig {

    @Bean  // declares the PasswordEncoder bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  
    }

}