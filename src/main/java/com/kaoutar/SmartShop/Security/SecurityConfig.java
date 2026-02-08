package com.kaoutar.SmartShop.Security;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final JwtFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
          http
                .csrf((csrf)->csrf.disable())
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers("/api/auth/*").permitAll()
                                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/client/**").hasAuthority("CLIENT")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                  .exceptionHandling(exception -> exception
                          .authenticationEntryPoint(customAuthenticationEntryPoint())
                          .accessDeniedHandler(customAccessDeniedHandler())
                  )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


                return  http.build();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now().toString());
            error.put("status", 401);
            error.put("error", "Unauthorized");
            error.put("message", "Authentication required: " + authException.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
        };
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now().toString());
            error.put("status", 403);
            error.put("error", "Forbidden");
            error.put("message", "Access denied: " + accessDeniedException.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
        };
    }

    private AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
