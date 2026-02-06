package com.kaoutar.SmartShop.Security;

import jakarta.servlet.http.HttpServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        return  http
                .csrf((csrf)->csrf.disable())
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers("/api/auth/*").permitAll()
                                .anyRequest().authenticated()
                )



                .build();
    }
}
