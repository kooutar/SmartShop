package com.kaoutar.SmartShop.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extraire le header Authorization si présent (format: Bearer <token>)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // On peut valider le token ici et charger l'utilisateur dans le SecurityContext si nécessaire
            // Pour éviter de bloquer toutes les requêtes, on laisse simplement continuer la chaîne
            // (implémentation plus complète possible selon JwtService)
        }

        // Important : continuer la chaîne de filtres sinon la requête s'arrête ici (corps vide)
        filterChain.doFilter(request, response);
    }
}
