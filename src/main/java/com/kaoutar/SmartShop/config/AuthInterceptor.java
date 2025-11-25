package com.kaoutar.SmartShop.config;

import com.kaoutar.SmartShop.enums.UserRole;
import com.kaoutar.SmartShop.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        HttpSession session = request.getSession(false);


        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez vous connecter");
            return false;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session invalide");
            return false;
        }

        String uri = request.getRequestURI();


        if (!hasAccess(uri, user)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès interdit");
            return false;
        }

        return true;
    }

    private boolean hasAccess(String uri, User user) {

        return switch (user.getRole()) {
            case ADMIN -> true; // ADMIN a accès partout
            case CLIENT -> !uri.startsWith("/api/admin"); // CLIENT interdit sur /api/admin/**
            default -> false;
        };
    }


}
