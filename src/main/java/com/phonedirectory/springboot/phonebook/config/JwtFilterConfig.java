package com.phonedirectory.springboot.phonebook.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonedirectory.springboot.phonebook.jwt.JWTUtil;
import com.phonedirectory.springboot.phonebook.jwt.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(
            JWTUtil jwtUtil,
            ObjectMapper objectMapper) {
        return new JWTAuthenticationFilter(jwtUtil, objectMapper);
    }
}