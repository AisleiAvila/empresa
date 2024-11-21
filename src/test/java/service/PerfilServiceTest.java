package service;

import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.repository.PerfilRepository;
import com.dasad.empresa.service.PerfilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private PerfilService perfilService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<PerfilModel> perfis = List.of(new PerfilModel());
        when(perfilRepository.findAll()).thenReturn(perfis);

        List<PerfilModel> result = perfilService.findAll();

        assertNotNull(result);
        assertEquals(perfis, result);
    }
}
