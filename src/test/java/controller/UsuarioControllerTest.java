//package controller;
//
//import com.dasad.empresa.controller.UsuarioController;
//import com.dasad.empresa.service.UsuarioService;
//import com.dasad.empresa.model.UsuarioRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UsuarioControllerTest {
//
//    @Mock
//    UsuarioService usuarioService;
//
//    @InjectMocks
//    UsuarioController usuarioController;
//
//    @BeforeEach
//    void setup() {
//        // Executa antes de cada teste, se for necessário algum setup adicional
//    }
//
//    @Nested
//    class FindTests {
//
//        @Test
//        public void shouldReturnSuccess() {
//            // Act
//            var response = usuarioController.findUsuario(null);
//            // Assert
//            assertNotNull(response, "A resposta não deve ser nula");
//        }
//
////        @Test
////        public void shouldPassCorrectParameters() {
////            // Arrange
////            var nome = "nome";
////            var id = 1;
////            var email = "email@email.com";
////            var dataNascimento = LocalDate.now();
////
////            var usuarioRequest = UsuarioRequest.Builder
////                    .aUnidadeFederativaRequest()
////                    .withId(id)
////                    .withNome(nome)
////                    .withEmail(email)
////                    .withDataNascimento(dataNascimento)
////                    .build();
////
////            // Act
////            var response = usuarioController.findUsuario(usuarioRequest);
////
////            // Assert
////            assertNotNull(response, "A resposta não deve ser nula");
////        }
//
//        @Test
//        public void shouldReturnError() {
//            // Arrange
//            UsuarioRequest usuarioRequest = new UsuarioRequest();
//            when(usuarioService.find(usuarioRequest)).thenThrow(new RuntimeException("Erro interno do servidor"));
//
//            // Act & Assert
//            assertThrows(RuntimeException.class, () -> usuarioController.findUsuario(usuarioRequest),
//                    "Deveria lançar RuntimeException");
//        }
//    }
//}
