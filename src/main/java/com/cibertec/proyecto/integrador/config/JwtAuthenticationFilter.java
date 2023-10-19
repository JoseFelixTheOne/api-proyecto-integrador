package com.cibertec.proyecto.integrador.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JWTProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        System.out.println("Analiza");

        String token = extractToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // Permitir el acceso a ciertas rutas sin autenticación
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        if ("/api/loginusertoken".equals(requestURI) || "/api/otra-ruta-publica".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Restringir el acceso a otras rutas que no sean públicas
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Acceso no autorizado");
    }

    private String extractToken(jakarta.servlet.http.HttpServletRequest request) {
        // Lógica para extraer el token del encabezado de la solicitud o del cuerpo, según sea tu caso.
        // Devuelve null si el token no se encuentra en la solicitud.
        return "";
    }


}
