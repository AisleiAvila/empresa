package com.dasad.empresa.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.model.UsuarioModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AuthorizationService {
    private static final long JWT_EXPIRATION = 2 * 60 * 60 * 1000; // 2 hours in milliseconds

    @Value("${api.security.token.secret}")
    private String secret;

    private Set<String> revokedTokens = new HashSet<>();

    public AuthorizationService() {
    }

    public String generateToken(UsuarioModel usuarioModel) {
        if (!StringUtils.hasText(this.secret)) {
            log.error("Token secret is not configured properly.");
            throw new IllegalStateException("Token secret is not configured properly.");
        }

        List<String> roles = usuarioModel.getPerfis().stream()
                .map(PerfilModel::getNome)
                .collect(Collectors.toList());
        String rolesString = String.join(",", roles);
        return JWT.create()
                .withSubject(usuarioModel.getEmail())
                .withIssuer("login-auth-api")
                .withClaim("role", rolesString)
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .sign(Algorithm.HMAC512(this.secret.getBytes()));
    }

    public String validateToken(String token) {

        if (!StringUtils.hasText(token)) {
            log.error("Token inexistente ");
            return null;
        }

        if (revokedTokens.contains(token)) {
            log.error("Token has been revoked");
            return null;
        }

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
                log.info("Autenticação bem-sucedida para o usuário: {} com roles: {}", userEmail, rolesString);
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

    public void revokeToken(String token) {
        log.info("Revogando token");
        revokedTokens.add(token);
    }

    private Instant getExpirationTime() {
        log.info("Calculating token expiration time");
        return LocalDateTime.now().plusHours(2L).toInstant(ZoneOffset.of("-03:00"));
    }
}