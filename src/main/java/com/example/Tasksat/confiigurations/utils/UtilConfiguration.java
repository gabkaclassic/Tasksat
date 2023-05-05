package com.example.Tasksat.confiigurations.utils;

import com.example.Tasksat.handling.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class UtilConfiguration {

    @Bean
    @Scope("singleton")
    public JWTUtil jwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") String expiration) {

        return new JWTUtil(secret, expiration);
    }

}
