package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 🔓 Desactivamos protección CSRF (solo en desarrollo)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 🔓 Todas las rutas públicas temporalmente
            );

        return http.build();
    }
}
