package service;

import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.UsuarioRequest;
import com.dasad.empresa.repository.UsuarioRecuperarSenhaRepository;
import com.dasad.empresa.repository.UsuarioRepository;
import com.dasad.empresa.service.UsuarioService;
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
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRecuperarSenhaRepository tokenRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFind() {
        UsuarioRequest request = new UsuarioRequest();
        List<UsuarioModel> usuarios = List.of(new UsuarioModel());
        when(usuarioRepository.find(any(UsuarioRequest.class))).thenReturn(Optional.of(usuarios));

        Optional<List<UsuarioModel>> result = usuarioService.find(request);

        assertTrue(result.isPresent());
        assertEquals(usuarios, result.get());
    }

    @Test
    void testCountTotalRecords() {
        UsuarioRequest request = new UsuarioRequest();
        when(usuarioRepository.countTotalRecords(any(UsuarioRequest.class))).thenReturn(Optional.of(10));

        Optional<Integer> result = usuarioService.countTotalRecords(request);

        assertTrue(result.isPresent());
        assertEquals(10, result.get());
    }

    @Test
    void testFindById() {
        UsuarioModel usuario = new UsuarioModel();
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));

        Optional<UsuarioModel> result = usuarioService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
    }

    @Test
    void testFindByEmail() {
        UsuarioModel usuario = new UsuarioModel();
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        Optional<UsuarioModel> result = usuarioService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
    }

    @Test
    void testCreate() {
        UsuarioModel usuario = new UsuarioModel();
        when(usuarioRepository.create(any(UsuarioModel.class))).thenReturn(usuario);

        UsuarioModel result = usuarioService.create(usuario);

        assertNotNull(result);
        assertEquals(usuario, result);
    }

    @Test
    void testUpdate() {
        UsuarioModel usuario = new UsuarioModel();
        when(usuarioRepository.update(any(UsuarioModel.class))).thenReturn(usuario);

        UsuarioModel result = usuarioService.update(usuario);

        assertNotNull(result);
        assertEquals(usuario, result);
    }

//    @Test
//    void testDeleteById() {
//        doNothing().when(usuarioRepository).deleteById(anyInt());
//
//        usuarioService.deleteById(1);
//
//        verify(usuarioRepository, times(1)).deleteById(1);
//    }

    @Test
    void testUpdatePassword() {
        doNothing().when(usuarioRepository).updatePassword(anyInt(), anyString());

        usuarioService.updatePassword(1, "newPassword");

        verify(usuarioRepository, times(1)).updatePassword(1, "newPassword");
    }
}
