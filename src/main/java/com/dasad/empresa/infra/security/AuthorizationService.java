package com.dasad.empresa.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.model.UsuarioModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

@Service
public class AuthorizationService {
    private static final long JWT_EXPIRATION = 2 * 60 * 60 * 1000; // 2 hours in milliseconds
    private static final Logger log = LogManager.getLogger(AuthorizationService.class);

    @Value("${api.security.token.secret}")
    private String secret;

    public AuthorizationService() {
    }

    public String generateToken(UsuarioModel usuario) {
        if (!StringUtils.hasText(this.secret)) {
            log.error("Token secret is not configured properly.");
            throw new IllegalStateException("Token secret is not configured properly.");
        }

        List<String> roles = usuario.getPerfis().stream()
                .map(PerfilModel::getNome)
                .collect(Collectors.toList());
        String rolesString = String.join(",", roles);
        return JWT.create()
                .withSubject(usuario.getEmail())
                .withIssuer("login-auth-api")
                .withClaim("role", rolesString)
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .sign(Algorithm.HMAC512(this.secret.getBytes()));
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(this.secret.getBytes());
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token);
            Instant expirationTime = jwt.getExpiresAt().toInstant();
            long minutesUntilExpiration = Duration.between(Instant.now(), expirationTime).toMinutes();
            log.info("Tempo restante até a expiração do token: {} minutos", minutesUntilExpiration);

            String userEmail = jwt.getSubject();
            String rolesString = jwt.getClaim("role").asString();
            if (StringUtils.hasText(userEmail) && StringUtils.hasText(rolesString)) {
                List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userEmail, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Autenticação bem-sucedida para o usuário: " + userEmail + " com roles: " + rolesString);
                return userEmail;
            } else {
                log.error("Token inválido ou expirado");
                return null;
            }
        } catch (JWTVerificationException e) {
            log.error("Token verification failed", e);
            return null;
        }
    }

    private Instant getExpirationTime() {
        return LocalDateTime.now().plusHours(2L).toInstant(ZoneOffset.of("-03:00"));
    }
}