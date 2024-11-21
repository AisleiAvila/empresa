package service;

import com.dasad.empresa.model.EnderecoModel;
import com.dasad.empresa.repository.EnderecoRepository;
import com.dasad.empresa.service.EnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<EnderecoModel> enderecos = List.of(new EnderecoModel());
        when(enderecoRepository.findAll()).thenReturn(enderecos);

        List<EnderecoModel> result = enderecoService.findAll();

        assertNotNull(result);
        assertEquals(enderecos, result);
    }

    @Test
    void testFindById() {
        EnderecoModel endereco = new EnderecoModel();
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(endereco));

        Optional<EnderecoModel> result = enderecoService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(endereco, result.get());
    }

    @Test
    void testSave() {
        EnderecoModel endereco = new EnderecoModel();
        when(enderecoRepository.save(any(EnderecoModel.class))).thenReturn(endereco);

        EnderecoModel result = enderecoService.save(endereco);

        assertNotNull(result);
        assertEquals(endereco, result);
    }

    @Test
    void testDeleteById() {
        doNothing().when(enderecoRepository).deleteById(anyInt());

        enderecoService.deleteById(1);

        verify(enderecoRepository, times(1)).deleteById(1);
    }
}