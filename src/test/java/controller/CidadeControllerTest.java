package controller;

import com.dasad.empresa.controller.CidadeController;
import com.dasad.empresa.model.CidadeModel;
import com.dasad.empresa.model.EstadoModel;
import com.dasad.empresa.model.PaisModel;
import com.dasad.empresa.service.CidadeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CidadeControllerTest {

    @Mock
    private CidadeService cidadeService;

    @InjectMocks
    private CidadeController cidadeController;

    @Test
    void detailCidade_WhenCidadeExists_ReturnsOkWithCidade() {
        // Arrange
        Integer id = 1;
        PaisModel pais = new PaisModel();
        pais.setId(1);
        pais.setNome("Brasil");
        EstadoModel estado = new EstadoModel();
        estado.setPaisId(pais);
        estado.setId(1);
        estado.setNome("São Paulo");
        CidadeModel cidade = new CidadeModel();
        cidade.setId(id);
        cidade.setNome("São Paulo");
        cidade.setEstadoId(estado);
        when(cidadeService.findById(id)).thenReturn(Optional.of(cidade));

        // Act
        ResponseEntity<CidadeModel> response = cidadeController.detailCidade(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cidade, response.getBody());
        verify(cidadeService, times(1)).findById(id);
    }

    @Test
    void detailCidade_WhenCidadeNotExists_ReturnsNotFound() {
        // Arrange
        Integer id = 99;
        when(cidadeService.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<CidadeModel> response = cidadeController.detailCidade(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cidadeService, times(1)).findById(id);
    }

    @Test
    void findCidade_WithNomeAndEstadoId_ReturnsFilteredList() {
        // Arrange
        String nome = "São Paulo";
        Integer estadoId = 1;
        CidadeModel cidade = new CidadeModel();
        cidade.setId(1);
        cidade.setNome(nome);
        EstadoModel estado = new EstadoModel();
        estado.setId(estadoId);
        cidade.setEstadoId(estado);
        List<CidadeModel> expected = Collections.singletonList(cidade);
        when(cidadeService.findAll(nome, estadoId)).thenReturn(expected);

        // Act
        ResponseEntity<List<CidadeModel>> response = cidadeController.findCidade(nome, estadoId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(cidadeService, times(1)).findAll(nome, estadoId);
    }

    @Test
    void findCidade_WithNomeOnly_ReturnsFilteredList() {
        // Arrange
        Integer id = 1;
        PaisModel pais = new PaisModel();
        pais.setId(1);
        pais.setNome("Brasil");
        EstadoModel estado = new EstadoModel();
        estado.setPaisId(pais);
        estado.setId(1);
        estado.setNome("São Paulo");
        CidadeModel cidade = new CidadeModel();
        cidade.setId(id);
        cidade.setNome("São Paulo");
        cidade.setEstadoId(estado);
        List<CidadeModel> expected = Collections.singletonList(cidade);
        when(cidadeService.findAll(cidade.getNome(), null)).thenReturn(expected);

        // Act
        ResponseEntity<List<CidadeModel>> response = cidadeController.findCidade(cidade.getNome(), null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(cidadeService, times(1)).findAll(cidade.getNome(), null);
    }

    @Test
    void findCidade_WithEstadoIdOnly_ReturnsFilteredList() {
        // Arrange
        Integer id = 1;
        PaisModel pais = new PaisModel();
        pais.setId(1);
        pais.setNome("Brasil");
        EstadoModel estado = new EstadoModel();
        estado.setPaisId(pais);
        estado.setId(1);
        estado.setNome("São Paulo");
        CidadeModel cidade = new CidadeModel();
        cidade.setId(id);
        cidade.setNome("São Paulo");
        cidade.setEstadoId(estado);
        List<CidadeModel> expected = Collections.singletonList(cidade);
        when(cidadeService.findAll(null, cidade.getEstadoId().getId())).thenReturn(expected);

        // Act
        ResponseEntity<List<CidadeModel>> response = cidadeController.findCidade(null, cidade.getEstadoId().getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(cidadeService, times(1)).findAll(null, cidade.getEstadoId().getId());
    }

    @Test
    void findCidade_WithNoParameters_ReturnsAllCidades() {
        // Arrange
        Integer id = 1;
        PaisModel pais = new PaisModel();
        pais.setId(1);
        pais.setNome("Brasil");
        EstadoModel estado = new EstadoModel();
        estado.setPaisId(pais);
        estado.setId(1);
        estado.setNome("São Paulo");
        CidadeModel cidade1 = new CidadeModel();
        cidade1.setId(id);
        cidade1.setNome("São Paulo");
        cidade1.setEstadoId(estado);

        CidadeModel cidade2 = new CidadeModel();
        cidade2.setId(2);
        cidade2.setNome("Bauru");
        cidade2.setEstadoId(estado);

        List<CidadeModel> expected = List.of(cidade1, cidade2);
        when(cidadeService.findAll(null, null)).thenReturn(expected);

        // Act
        ResponseEntity<List<CidadeModel>> response = cidadeController.findCidade(null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(cidadeService, times(1)).findAll(null, null);
    }
}