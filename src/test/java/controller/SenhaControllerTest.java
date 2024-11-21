package controller;

import com.dasad.empresa.controller.SenhaController;
import com.dasad.empresa.model.RecuperarSenha200Response;
import com.dasad.empresa.model.RecuperarSenhaRequest;
import com.dasad.empresa.model.SalvarSenha200Response;
import com.dasad.empresa.model.SalvarSenhaRequest;
import com.dasad.empresa.model.ValidarResetToken200Response;
import com.dasad.empresa.service.SenhaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SenhaControllerTest {

    @Mock
    private SenhaService senhaService;

    @InjectMocks
    private SenhaController senhaController;

    private RecuperarSenhaRequest recuperarSenhaRequest;
    private SalvarSenhaRequest salvarSenhaRequest;
    private String token;

    @BeforeEach
    void setup() {
        recuperarSenhaRequest = new RecuperarSenhaRequest();
        salvarSenhaRequest = new SalvarSenhaRequest();
        token = "test-token";
    }

    @Nested
    class RecuperarSenhaTests {

        @Test
        public void shouldRecuperarSenhaSuccessfully() {
            // Arrange
            RecuperarSenha200Response responseMock = new RecuperarSenha200Response();
            when(senhaService.getRecuperarSenha(any(RecuperarSenhaRequest.class))).thenReturn(ResponseEntity.ok(responseMock));

            // Act
            ResponseEntity<RecuperarSenha200Response> response = senhaController.recuperarSenha(recuperarSenhaRequest);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.ok(responseMock), response);
        }
    }

    @Nested
    class SalvarSenhaTests {

        @Test
        public void shouldSalvarSenhaSuccessfully() {
            // Arrange
            SalvarSenha200Response responseMock = new SalvarSenha200Response();
            when(senhaService.getSalvarSenha(any(SalvarSenhaRequest.class))).thenReturn(ResponseEntity.ok(responseMock));

            // Act
            ResponseEntity<SalvarSenha200Response> response = senhaController.salvarSenha(salvarSenhaRequest);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.ok(responseMock), response);
        }
    }

    @Nested
    class ValidarResetTokenTests {

        @Test
        public void shouldValidarResetTokenSuccessfully() {
            // Arrange
            ValidarResetToken200Response responseMock = new ValidarResetToken200Response();
            when(senhaService.getValidarResetToken(token)).thenReturn(ResponseEntity.ok(responseMock));

            // Act
            ResponseEntity<ValidarResetToken200Response> response = senhaController.validarResetToken(token);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.ok(responseMock), response);
        }
    }
}