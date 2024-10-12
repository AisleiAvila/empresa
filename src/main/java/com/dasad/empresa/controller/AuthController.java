package com.dasad.empresa.controller;

import com.dasad.empresa.dto.LoginRequestDTO;
import com.dasad.empresa.dto.LoginResponseDTO;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Auth Controller", description = "Endpoints for authentication")
@RestController
@RequestMapping({"/auth"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    @Operation(summary = "Login endpoint")    @PostMapping({"/login"})
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        Optional<com.dasad.empresa.model.UsuarioModel> optionalUsuario = this.usuarioRepository.findByEmail(body.email());
        if (optionalUsuario.isPresent()) {
            com.dasad.empresa.model.UsuarioModel usuario = (com.dasad.empresa.model.UsuarioModel)optionalUsuario.get();
            if (this.passwordEncoder.matches(body.senha(), usuario.getSenha())) {
                String authorization = this.authorizationService.generateToken(usuario);
                return ResponseEntity.ok(new LoginResponseDTO(usuario.getNome(), authorization));
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Verify authorization endpoint")
    @GetMapping({"/verify-authorization"})
    public ResponseEntity<Boolean> verifyAuthorization(@RequestHeader("Authorization") String authorization) {
        if (authorization == null) {
            return ResponseEntity.badRequest().build();
        } else {
            String authorized = this.authorizationService.validateToken(authorization);
            return authorized != null ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Register endpoint")
    @PostMapping({"/register"})
    public ResponseEntity register(@RequestBody com.dasad.empresa.model.RegisterRequestDTO body) {
        Optional<com.dasad.empresa.model.UsuarioModel> usuarioCadastrado = this.usuarioRepository.findByEmail(body.getEmail());
        if (usuarioCadastrado.isEmpty()) {
            com.dasad.empresa.model.UsuarioModel usuario = new com.dasad.empresa.model.UsuarioModel();
            usuario.setNome(body.getNome());
            usuario.setEmail(body.getEmail());
            usuario.setSenha(this.passwordEncoder.encode(body.getSenha()));
            usuario.setDataNascimento(body.getDataNascimento());
            usuario.setEnderecos(body.getEnderecos());
            usuario.setPerfis(body.getPerfis());

            this.usuarioRepository.create(usuario);
            String authorization = this.authorizationService.generateToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(usuario.getNome(), authorization));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    public AuthController(final UsuarioRepository usuarioRepository, final PasswordEncoder passwordEncoder, final AuthorizationService authorizationService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }
}
