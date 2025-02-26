package controller;

import com.dasad.empresa.controller.AuthController;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.model.LoginRequestDTO;
import com.dasad.empresa.model.LoginResponseDTO;
import com.dasad.empresa.model.RegisterRequestDTO;
import com.dasad.empresa.model.RevokeToken200Response;
import com.dasad.empresa.model.RevokeTokenRequest;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private AuthController authController;

    private LoginRequestDTO loginRequestDTO;
    private RegisterRequestDTO registerRequestDTO;
    private RevokeTokenRequest revokeTokenRequest;
    private UsuarioModel usuarioModel;

    @BeforeEach
    void setup() {
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("email@teste.com");
        loginRequestDTO.setSenha("senha123");

        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail("email@teste.com");
        registerRequestDTO.setNome("Nome Teste");
        registerRequestDTO.setSenha("senha123");

        revokeTokenRequest = new RevokeTokenRequest();
        revokeTokenRequest.setToken("test-token");

        usuarioModel = new UsuarioModel();
        usuarioModel.setEmail("email@teste.com");
        usuarioModel.setNome("Nome Teste");
        usuarioModel.setSenha("senha123");
    }

    @Nested
    class VerifyAuthorizationTests {

        @Test
        public void shouldVerifyAuthorizationSuccessfully() {
            // Arrange
            String token = "valid-token";
            when(authorizationService.validateToken(token)).thenReturn("authorized");

            // Act
            ResponseEntity<Boolean> response = authController.verifyAuthorization(token);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.ok(true), response);
        }

        @Test
        public void shouldReturnBadRequestWhenAuthorizationIsNull() {
            // Arrange
            String token = "invalid-token";
            when(authorizationService.validateToken(token)).thenReturn(null);

            // Act
            ResponseEntity<Boolean> response = authController.verifyAuthorization(token);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.badRequest().build(), response);
        }
    }

    @Nested
    class LoginTests {

//        @Test
//        public void shouldLoginSuccessfully() {
//            // Arrange
//            when(usuarioRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(usuarioModel));
//            when(passwordEncoder.matches(loginRequestDTO.getSenha(), usuarioModel.getSenha())).thenReturn(true);
//            when(authorizationService.generateToken(usuarioModel)).thenReturn("valid-token");
//
//            // Act
//            ResponseEntity<LoginResponseDTO> response = authController.login(loginRequestDTO);
//
//            // Assert
//            assertNotNull(response);
//            assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
//            assertNotNull(response.getBody());
//            assertEquals("valid-token", response.getBody().getAuthorization());
//        }

        @Test
        public void shouldReturnBadRequestWhenPasswordDoesNotMatch() {
            // Arrange
            when(usuarioRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(usuarioModel));
            when(passwordEncoder.matches(loginRequestDTO.getSenha(), usuarioModel.getSenha())).thenReturn(false);

            // Act
            ResponseEntity<LoginResponseDTO> response = authController.login(loginRequestDTO);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
        }

        @Test
        public void shouldReturnBadRequestWhenUserIsNotPresent() {
            // Arrange
            when(usuarioRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.empty());

            // Act
            ResponseEntity<LoginResponseDTO> response = authController.login(loginRequestDTO);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
        }
    }
    @Nested
    class RegisterTests {

//        @Test
//        public void shouldRegisterSuccessfully() {
//            // Arrange
//            when(usuarioRepository.findByEmail(registerRequestDTO.getEmail())).thenReturn(Optional.empty());
//            when(passwordEncoder.encode(registerRequestDTO.getSenha())).thenReturn("encoded-password");
//            when(authorizationService.generateToken(any(UsuarioModel.class))).thenReturn("valid-token");
//
//            // Act
//            ResponseEntity<LoginResponseDTO> response = authController.register(registerRequestDTO);
//
//            // Assert
//            assertNotNull(response);
//            assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
//            assertNotNull(response.getBody());
//            assertEquals("valid-token", response.getBody().getAuthorization());
//        }

        @Test
        public void shouldReturnBadRequestWhenUserAlreadyExists() {
            // Arrange
            when(usuarioRepository.findByEmail(registerRequestDTO.getEmail())).thenReturn(Optional.of(usuarioModel));

            // Act
            ResponseEntity<LoginResponseDTO> response = authController.register(registerRequestDTO);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
        }
    }

    @Nested
    class RevokeTokenTests {

        @Test
        public void shouldRevokeTokenSuccessfully() {
            // Act
            ResponseEntity<RevokeToken200Response> response = authController.revokeToken(revokeTokenRequest);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        }

        @Test
        public void shouldThrowIllegalArgumentExceptionWhenTokenIsNullOrEmpty() {
            // Arrange
            revokeTokenRequest.setToken(null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                authController.revokeToken(revokeTokenRequest);
            });

            // Arrange
            revokeTokenRequest.setToken("");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                authController.revokeToken(revokeTokenRequest);
            });
        }
    }
}
