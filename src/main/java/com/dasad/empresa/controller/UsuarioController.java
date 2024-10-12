package com.dasad.empresa.controller;

import com.dasad.empresa.api.UsuarioApi;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.UsuarioRequest;
import com.dasad.empresa.model.UsuarioResponseDTO;
import com.dasad.empresa.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
public class UsuarioController implements UsuarioApi{
    @Autowired
    private UsuarioService usuarioService;

    @Override
    @PutMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<UsuarioModel> createUsuario(com.dasad.empresa.model.RegisterRequestDTO registerRequestDTO) {

        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(registerRequestDTO.getId());
        usuario.setNome(registerRequestDTO.getNome());
        usuario.setEmail(registerRequestDTO.getEmail());
        usuario.setSenha(registerRequestDTO.getSenha());
        usuario.setDataNascimento(registerRequestDTO.getDataNascimento());
        usuario.setEnderecos(registerRequestDTO.getEnderecos());
        usuario.setPerfis(registerRequestDTO.getPerfis());

        return ResponseEntity.ok(this.usuarioService.create(usuario));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Void> deleteUsuario(Integer id) {
        if (this.usuarioService.findById(id).isPresent()) {
            this.usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping("/find")
    @PreAuthorize("hasAnyRole('Administrador', 'Moderador', 'Usuário')")
    public ResponseEntity<UsuarioResponseDTO> findUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        // Implemente a lógica aqui
        try {
            Optional<List<UsuarioModel>> usuarios = this.usuarioService.find(usuarioRequest);
            var totalRecords = 0;
            if (usuarioRequest.getOffset() == null || usuarioRequest.getOffset() == 0) {
                var retorno = this.usuarioService.countTotalRecords(usuarioRequest);
                totalRecords = retorno.orElse(0);
            }
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setUsuarios(usuarios.orElse(Collections.emptyList()));
            responseDTO.setTotalRecords(totalRecords);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro interno do servidor", e);
        }
    }

    @Override
    @PatchMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<UsuarioModel> updateUsuario(UsuarioModel usuarioModel) {
        var usuario = this.usuarioService.update(usuarioModel);
        return ResponseEntity.ok(usuario);
    }
}