package com.kaoutar.SmartShop.config;

import com.kaoutar.SmartShop.enums.UserRole;
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

        Long userId=(Long) session.getAttribute("userId");
        UserRole userRole = (UserRole) session.getAttribute("role");
        if (userId == null && userRole==null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session invalide");
            return false;
        }

        String uri = request.getRequestURI();


        if (!hasAccess(uri, userId, userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès interdit");
            return false;
        }

        return true;
    }

    private boolean hasAccess(String uri, Long user, UserRole userRole) {

        return switch (userRole) {
            case ADMIN -> true;
            case CLIENT -> !uri.startsWith("/api/admin");
            default -> false;
        };
    }


}
