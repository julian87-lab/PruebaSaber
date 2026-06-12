package com.pruebasaber.app.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean esAdmin = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        boolean esCoordinador = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_COORDINADOR"::equals);

        boolean esDocente = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_DOCENTE"::equals);

        boolean esEstudiante = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ESTUDIANTE"::equals);

        if (esAdmin) {
            response.sendRedirect("/dashboard/admin");
        } else if (esCoordinador) {
            response.sendRedirect("/dashboard/coordinador");
        } else if (esDocente) {
            response.sendRedirect("/dashboard/docente");
        } else if (esEstudiante) {
            response.sendRedirect("/dashboard/estudiante");
        } else {
            response.sendRedirect("/login");
        }
    }
}