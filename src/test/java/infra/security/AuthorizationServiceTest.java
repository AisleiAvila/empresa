package infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.model.UsuarioModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UsuarioModel usuario;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(authorizationService, "secret", "mySecretKey");
    }

    @Test
    public void testGenerateToken() {
        PerfilModel perfil = new PerfilModel();
        perfil.setNome("USER");
        when(usuario.getEmail()).thenReturn("user@example.com");
        when(usuario.getPerfis()).thenReturn(Collections.singletonList(perfil));

        String token = authorizationService.generateToken(usuario);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testGenerateTokenWithoutSecret() {
        ReflectionTestUtils.setField(authorizationService, "secret", "");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authorizationService.generateToken(usuario);
        });

        assertEquals("Token secret is not configured properly.", exception.getMessage());
    }

    @Test
    public void testValidateToken() {
        PerfilModel perfil = new PerfilModel();
        perfil.setNome("USER");
        when(usuario.getEmail()).thenReturn("user@example.com");
        when(usuario.getPerfis()).thenReturn(Collections.singletonList(perfil));

        String token = authorizationService.generateToken(usuario);
        String userEmail = authorizationService.validateToken(token);

        assertNotNull(userEmail);
        assertEquals("user@example.com", userEmail);
    }

    @Test
    public void testValidateTokenWithInvalidToken() {
        // Token malformado com três partes e base64 válido
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.invalid.payload";

        String userEmail = authorizationService.validateToken(invalidToken);

        assertNull(userEmail);
    }

//    @Test
//    public void testValidateTokenWithExpiredToken() {
//        // Gerar um token com uma data de expiração curta
//        PerfilModel perfil = new PerfilModel();
//        perfil.setNome("USER");
//        when(usuario.getEmail()).thenReturn("user@example.com");
//        when(usuario.getPerfis()).thenReturn(Collections.singletonList(perfil));
//
//        String token = JWT.create()
//                .withSubject(usuario.getEmail())
//                .withIssuer("login-auth-api")
//                .withClaim("role", "USER")
//                .withExpiresAt(new Date(System.currentTimeMillis() - 1000)) // Expirado
//                .sign(Algorithm.HMAC512("mySecretKey".getBytes()));
//
//        String userEmail = authorizationService.validateToken(token);
//
//        assertNull(userEmail);
//    }
}