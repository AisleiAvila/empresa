package com.dasad.empresa.controller;

import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/usuarios"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    public UsuarioController() {
    }

    @GetMapping
    @CrossOrigin(
            origins = {"http://localhost:4200", "http://localhost:8080"}
    )
    public List<UsuarioModel> findAll() {
        return this.usuarioService.findAll();
    }

    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<UsuarioModel> getUsuarioById(@PathVariable Integer id) {
        return (ResponseEntity)this.usuarioService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsuarioModel createUsuario(@RequestBody UsuarioModel usuario) {
        return this.usuarioService.save(usuario);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<UsuarioModel> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioModel usuarioDetails) {
        return (ResponseEntity)this.usuarioService.findById(id).map((usuario) -> {
            usuario.setNome(usuarioDetails.getNome());
            usuario.setDataNascimento(usuarioDetails.getDataNascimento());
            UsuarioModel updatedUsuario = this.usuarioService.save(usuario);
            return ResponseEntity.ok(updatedUsuario);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        if (this.usuarioService.findById(id).isPresent()) {
            this.usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
