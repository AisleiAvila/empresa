package com.dasad.empresa.controller;

import com.dasad.empresa.dto.RegisterRequestDTO;
import com.dasad.empresa.dto.UsuarioResponseDTO;
import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.models.request.UsuarioRequest;
import com.dasad.empresa.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public UsuarioResponseDTO find(@RequestBody UsuarioRequest usuarioRequest) {
        try {
            Optional<List<UsuarioModel>> usuarios = this.usuarioService.find(usuarioRequest);
            var totalRecords = 0;
            if (usuarioRequest.getOffset() == null || usuarioRequest.getOffset() == 0) {
               var retorno =  this.usuarioService.countTotalRecords(usuarioRequest);
                totalRecords = retorno.orElse(0);
            }
            return new UsuarioResponseDTO(usuarios.orElse(Collections.emptyList()), totalRecords);
        } catch (Exception e) {
            throw new RuntimeException("Erro interno do servidor", e);
        }
    }


    @PutMapping
    public UsuarioModel createUsuario(@RequestBody RegisterRequestDTO registerRequestDTO) {
        UsuarioModel usuario = new UsuarioModel(
                registerRequestDTO.nome(),
                registerRequestDTO.email(),
                registerRequestDTO.senha(),
                registerRequestDTO.dataNascimento(),
                registerRequestDTO.enderecos(),
                registerRequestDTO.perfis()
        );
        return this.usuarioService.create(usuario);
    }

    @PatchMapping
    public UsuarioModel updateUsuario(@RequestBody UsuarioModel usuarioDetails) {
        return this.usuarioService.update(usuarioDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        if (this.usuarioService.findById(id).isPresent()) {
            this.usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}