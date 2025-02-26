package infra.security;

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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UsuarioModel usuarioModel;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(authorizationService, "secret", "mySecretKey");
    }

    @Test
    public void testGenerateToken() {
        PerfilModel perfilModel = new PerfilModel();
        perfilModel.setNome("USER");
        when(usuarioModel.getEmail()).thenReturn("user@example.com");
        when(usuarioModel.getPerfis()).thenReturn(Collections.singletonList(perfilModel));

        String token = authorizationService.generateToken(usuarioModel);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testGenerateTokenWithoutSecret() {
        ReflectionTestUtils.setField(authorizationService, "secret", "");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authorizationService.generateToken(usuarioModel);
        });

        assertEquals("Token secret is not configured properly.", exception.getMessage());
    }

    @Test
    public void testValidateToken() {
        PerfilModel perfilModel = new PerfilModel();
        perfilModel.setNome("USER");
        when(usuarioModel.getEmail()).thenReturn("user@example.com");
        when(usuarioModel.getPerfis()).thenReturn(Collections.singletonList(perfilModel));

        String token = authorizationService.generateToken(usuarioModel);
        String userEmail = authorizationService.validateToken(token);

        assertNotNull(userEmail);
        assertEquals("user@example.com", userEmail);
    }

//    @Test
//    public void testValidateTokenWithInvalidToken() {
//        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.invalid.payload";
//
//        String userEmail = authorizationService.validateToken(invalidToken);
//
//        assertNull(userEmail);
//    }

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

    @Test
    public void testRevokeToken() {
        String token = "sampleToken";
        authorizationService.revokeToken(token);

        Set<String> revokedTokens = (Set<String>) ReflectionTestUtils.getField(authorizationService, "revokedTokens");
        assert revokedTokens != null;
        assertTrue(revokedTokens.contains(token));
    }

    @Test
    public void testRevokeTokenAlreadyRevoked() {
        String token = "sampleToken";
        authorizationService.revokeToken(token);
        authorizationService.revokeToken(token);

        Set<String> revokedTokens = (Set<String>) ReflectionTestUtils.getField(authorizationService, "revokedTokens");
        assert revokedTokens != null;
        assertTrue(revokedTokens.contains(token));
    }
}