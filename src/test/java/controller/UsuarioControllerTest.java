package controller;

import com.dasad.empresa.controller.UsuarioController;
import com.dasad.empresa.service.UsuarioService;
import org.junit.experimental.runners.Enclosed;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@RunWith(Enclosed.class)
@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    UsuarioController usuarioController;

    @Nested
    public class find {

//        @Test
//        public void shouldReturnSuccess() {
//            // Act
//            var response = usuarioController.find(null);
//            // Assert
//            assert response != null;
//        }

//        @Test
//        public void shouldPassCorrectParameters() {
//            // Arrange
//            var nome = "nome";
//            var id = 1;
//            var email = "email@email.com";
//            var dataNascimento = LocalDate.now();
//
//            var usuarioRequest = UsuarioRequest.Builder
//                    .aUnidadeFederativaRequest()
//                    .withId(id)
//                    .withNome(nome)
//                    .withEmail(email)
//                    .withDataNascimento(dataNascimento)
//                    .build();
//
//            // Act
//            var response = usuarioController.find(usuarioRequest);
//
//            // Assert
//            assert response != null;
//        }

//        @Test(expected = RuntimeException.class)
//        public void shouldReturnError() {
//            // Arrange
//            UsuarioRequest usuarioRequest = new UsuarioRequest();
//            when(usuarioService.find(usuarioRequest)).thenThrow(new RuntimeException("Erro interno do servidor"));
//
//            // Act
//            usuarioController.find(usuarioRequest);
//
//            // Assert
//            // A anotação @Test(expected = RuntimeException.class) verifica se a exceção é lançada
//        }
//
    }
}