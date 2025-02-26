package service;

import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.model.RecuperarSenha200Response;
import com.dasad.empresa.model.RecuperarSenhaRequest;
import com.dasad.empresa.model.SalvarSenha200Response;
import com.dasad.empresa.model.SalvarSenhaRequest;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.ValidarResetToken200Response;
import com.dasad.empresa.repository.SenhaRepository;
import com.dasad.empresa.repository.UsuarioRecuperarSenhaRepository;
import com.dasad.empresa.repository.UsuarioRepository;
import com.dasad.empresa.service.EmailService;
import com.dasad.empresa.service.SenhaService;
import com.dasad.empresa.service.UsuarioService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SenhaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private EmailService emailService;

    @Mock
    private SenhaRepository senhaRepository;

    @Mock
    private UsuarioRecuperarSenhaRepository tokenRepository;

    @InjectMocks
    private SenhaService senhaService;

    private UsuarioModel usuarioModel;
    private RecuperarSenhaRequest recuperarSenhaRequest;
    private SalvarSenhaRequest salvarSenhaRequest;

    @BeforeEach
    void setUp() {
        usuarioModel = new UsuarioModel();
        usuarioModel.setId(1);
        usuarioModel.setNome("Test User");
        usuarioModel.setEmail("test@example.com");

        recuperarSenhaRequest = new RecuperarSenhaRequest();
        recuperarSenhaRequest.setEmail("test@example.com");

        salvarSenhaRequest = new SalvarSenhaRequest();
        salvarSenhaRequest.setToken("valid-token");
        salvarSenhaRequest.setSenha("new-password");
        salvarSenhaRequest.setRepeatSenha("new-password");
    }

    @Test
    void testGetRecuperarSenha() throws MessagingException {
        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioModel));
        when(authorizationService.generateToken(any(UsuarioModel.class))).thenReturn("valid-token");

        ResponseEntity<RecuperarSenha200Response> response = senhaService.getRecuperarSenha(recuperarSenhaRequest);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(emailService, times(1)).sendPasswordResetEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testGetValidarResetToken() {
        when(authorizationService.validateToken(anyString())).thenReturn("test@example.com");
        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioModel));

        ResponseEntity<ValidarResetToken200Response> response = senhaService.getValidarResetToken("valid-token");

        assertNotNull(response);
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    void testGetSalvarSenha() {
        when(authorizationService.validateToken(anyString())).thenReturn("test@example.com");
        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioModel));

        ResponseEntity<SalvarSenha200Response> response = senhaService.getSalvarSenha(salvarSenhaRequest);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok().build().getStatusCode(), response.getStatusCode());
        verify(usuarioService, times(1)).updatePassword(anyInt(), anyString());
    }

    @Test
    void testGetRecuperarSenhaWithNullEmail() {
        // Arrange
        recuperarSenhaRequest.setEmail(null);

        // Act
        ResponseEntity<RecuperarSenha200Response> response = senhaService.getRecuperarSenha(recuperarSenhaRequest);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    void testGetRecuperarSenhaWithInvalidEmail() {
        // Arrange
        recuperarSenhaRequest.setEmail("invalid-email");

        // Act
        ResponseEntity<RecuperarSenha200Response> response = senhaService.getRecuperarSenha(recuperarSenhaRequest);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }

//    @Test
//    void testGetRecuperarSenhaWithMessagingException() throws MessagingException {
//        // Arrange
//        when(usuarioService.findByEmail(anyString())).thenReturn(Optional.of(usuarioModel));
//        when(authorizationService.generateToken(any(UsuarioModel.class))).thenReturn("valid-token");
//        doThrow(new MessagingException("Erro ao enviar e-mail")).when(emailService).sendPasswordResetEmail(anyString(), anyString(), anyString());
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            senhaService.getRecuperarSenha(recuperarSenhaRequest);
//        });
//
//        assertNotNull(exception.getCause());
//        assertInstanceOf(MessagingException.class, exception.getCause());
//        assertEquals("Erro ao enviar e-mail", exception.getCause().getMessage());
//    }

    @Test
    void testGetValidarResetTokenWithException() {
        // Arrange
        when(authorizationService.validateToken(anyString())).thenThrow(new RuntimeException("Erro ao validar token"));

        // Act
        ResponseEntity<ValidarResetToken200Response> response = senhaService.getValidarResetToken("invalid-token");

        // Assert
        assertNotNull(response);
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    void testGetValidarResetTokenWithNullEmailException() {
        // Arrange
        when(authorizationService.validateToken(anyString())).thenThrow(new RuntimeException("Erro ao validar token"));

        // Act
        ResponseEntity<ValidarResetToken200Response> response = senhaService.getValidarResetToken("invalid-token");

        // Assert
        assertNotNull(response);
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }
}
