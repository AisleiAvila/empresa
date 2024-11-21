package controller;

import com.dasad.empresa.controller.EnderecoController;
import com.dasad.empresa.model.EnderecoModel;
import com.dasad.empresa.service.EnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EnderecoControllerTest {

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private EnderecoController enderecoController;

    private EnderecoModel enderecoModel;

    @BeforeEach
    void setup() {
        enderecoModel = new EnderecoModel();
        enderecoModel.setId(1);
        enderecoModel.setLogradouro("Rua Teste");
        enderecoModel.setCidade("Cidade Teste");
        enderecoModel.setCep("12345-678");
    }

    @Nested
    class FindAllTests {

        @Test
        public void shouldReturnAllEnderecos() {
            // Arrange
            when(enderecoService.findAll()).thenReturn(Collections.singletonList(enderecoModel));

            // Act
            var response = enderecoController.findEndereco();

            // Assert
            assertNotNull(response);
            assertEquals(1, response.getBody().size() );
            assertEquals(enderecoModel, response.getBody().get(0));
        }
    }

    @Nested
    class GetEnderecoByIdTests {

        @Test
        public void shouldReturnEnderecoWhenExists() {
            // Arrange
            when(enderecoService.findById(1)).thenReturn(Optional.of(enderecoModel));

            // Act
            ResponseEntity<EnderecoModel> response = enderecoController.detailEndereco(1);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.ok(enderecoModel), response);
        }

        @Test
        public void shouldReturnNotFoundWhenEnderecoDoesNotExist() {
            // Arrange
            when(enderecoService.findById(1)).thenReturn(Optional.empty());

            // Act
            ResponseEntity<EnderecoModel> response = enderecoController.detailEndereco(1);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.notFound().build(), response);
        }
    }

    @Nested
    class CreateEnderecoTests {

        @Test
        public void shouldCreateEnderecoSuccessfully() {
            // Arrange
            when(enderecoService.save(any(EnderecoModel.class))).thenReturn(enderecoModel);

            // Act
            var response = enderecoController.createEndereco(enderecoModel);

            // Assert
            assertNotNull(response);
            assertEquals(enderecoModel, response.getBody());
        }
    }

    @Nested
    class UpdateEnderecoTests {

        @Test
        public void shouldUpdateEnderecoSuccessfully() {
            // Arrange
            when(enderecoService.findById(1)).thenReturn(Optional.of(enderecoModel));
            when(enderecoService.save(any(EnderecoModel.class))).thenReturn(enderecoModel);

            // Act
            ResponseEntity<EnderecoModel> response = enderecoController.updateEndereco(1, enderecoModel);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.ok(enderecoModel), response);
        }

        @Test
        public void shouldReturnNotFoundWhenEnderecoDoesNotExist() {
            // Arrange
            when(enderecoService.findById(1)).thenReturn(Optional.empty());

            // Act
            ResponseEntity<EnderecoModel> response = enderecoController.updateEndereco(1, enderecoModel);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.notFound().build(), response);
        }
    }

    @Nested
    class DeleteEnderecoTests {

        @Test
        public void shouldDeleteEnderecoSuccessfully() {
            // Arrange
            when(enderecoService.findById(1)).thenReturn(Optional.of(enderecoModel));
            doNothing().when(enderecoService).deleteById(1);

            // Act
            ResponseEntity<Void> response = enderecoController.deleteEndereco(1);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.noContent().build(), response);
        }

        @Test
        public void shouldReturnNotFoundWhenEnderecoDoesNotExist() {
            // Arrange
            when(enderecoService.findById(1)).thenReturn(Optional.empty());

            // Act
            ResponseEntity<Void> response = enderecoController.deleteEndereco(1);

            // Assert
            assertNotNull(response);
            assertEquals(ResponseEntity.notFound().build(), response);
        }
    }
}
