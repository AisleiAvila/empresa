package com.dasad.empresa.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dasad.empresa.models.UsuarioModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class AuthorizationService {
    private static final Logger log = LogManager.getLogger(AuthorizationService.class);
    @Value("${api.security.token.secret}")
    private String secret;

    public AuthorizationService() {
    }

    public String generateToken(UsuarioModel user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.create().withSubject(user.getEmail()).withIssuer("login-auth-api").withExpiresAt(Date.from(Instant.now().plus(Duration.ofHours(2L)))).sign(algorithm);
        } catch (JWTCreationException var3) {
            JWTCreationException exception = var3;
            throw new RuntimeException("Erro ao gerar token. " + exception.getMessage());
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            DecodedJWT jwt = JWT.require(algorithm).withIssuer("login-auth-api").build().verify(token);
            Instant expirationTime = jwt.getExpiresAt().toInstant();
            long minutesUntilExpiration = Duration.between(Instant.now(), expirationTime).toMinutes();
            log.error("Tempo restante até a expiração do token: {} minutos", minutesUntilExpiration);
            return jwt.getSubject();
        } catch (JWTVerificationException var7) {
            return null;
        }
    }

    private Instant getExpirationTime() {
        return LocalDateTime.now().plusHours(2L).toInstant(ZoneOffset.of("-03:00"));
    }
}
