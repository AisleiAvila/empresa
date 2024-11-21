package service;

import com.dasad.empresa.model.UnidadeFederativaModel;
import com.dasad.empresa.model.UnidadeFederativaRequest;
import com.dasad.empresa.repository.UnidadeFederativaRepository;
import com.dasad.empresa.service.UnidadeFederativaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnidadeFederativaServiceTest {

    @Mock
    private UnidadeFederativaRepository unidadeFederativaRepository;

    @InjectMocks
    private UnidadeFederativaService unidadeFederativaService;

    private UnidadeFederativaRequest request;
    private UnidadeFederativaModel model;

    @BeforeEach
    void setUp() {
        request = new UnidadeFederativaRequest();
        model = new UnidadeFederativaModel();
    }

    @Test
    void testFind() {
        List<UnidadeFederativaModel> expectedList = List.of(model);
        when(unidadeFederativaRepository.find(request)).thenReturn(Optional.of(expectedList));

        Optional<List<UnidadeFederativaModel>> result = unidadeFederativaService.find(request);

        assertEquals(Optional.of(expectedList), result);
        verify(unidadeFederativaRepository, times(1)).find(request);
    }
}