package com.dasad.empresa.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LogManager.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String endpoint = request.getRequestURI();

        log.info("Requisição recebida no JwtAuthenticationFilter: " + endpoint);

        // Excluir URLs do Swagger e endpoints de autenticação da autenticação
        if (endpoint.startsWith("/swagger-ui/") ||
                endpoint.startsWith("/v3/api-docs/") ||
                endpoint.equals("/api-docs/swagger-config") ||
                endpoint.equals("/auth/login") ||
                endpoint.equals("/auth/register") ||
                endpoint.equals("/lembrar-senha")) {
            log.info("URL excluída: " + endpoint);
            filterChain.doFilter(request, response);
            return;
        }

        if (authorizationHeader != null) {
            String token = authorizationHeader.replace("Bearer ", "");
            String userEmail = this.validateToken(token);
            if (userEmail != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, null, new ArrayList<>());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Autenticação bem-sucedida para o usuário: " + userEmail);
            } else {
                log.error("Token inválido ou expirado");
            }
        } else {
            log.error("Cabeçalho de autorização ausente ou malformado");
        }

        log.info("Antes de filterChain.doFilter no JwtAuthenticationFilter");
        filterChain.doFilter(request, response);
        log.info("Depois de filterChain.doFilter no JwtAuthenticationFilter");
    }

    private String validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            if (decodedJWT.getExpiresAt().before(new Date())) {
                log.error("Token expirado");
                return null;
            } else {
                return decodedJWT.getSubject();
            }
        } catch (JWTDecodeException e) {
            log.error("Erro ao decodificar o token: " + e.getMessage());
            return null;
        }
    }
}