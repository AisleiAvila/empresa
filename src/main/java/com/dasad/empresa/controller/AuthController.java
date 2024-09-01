package com.dasad.empresa.controller;

import com.dasad.empresa.dto.LoginRequestDTO;
import com.dasad.empresa.dto.LoginResponseDTO;
import com.dasad.empresa.dto.RegisterRequestDTO;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
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

@RestController
@RequestMapping({"/auth"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    @PostMapping({"/login"})
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        Optional<UsuarioModel> optionalUsuario = this.usuarioRepository.findByEmail(body.email());
        if (optionalUsuario.isPresent()) {
            UsuarioModel usuario = (UsuarioModel)optionalUsuario.get();
            if (this.passwordEncoder.matches(body.senha(), usuario.getSenha())) {
                String authorization = this.authorizationService.generateToken(usuario);
                return ResponseEntity.ok(new LoginResponseDTO(usuario.getNome(), authorization));
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping({"/verify-authorization"})
    public ResponseEntity<Boolean> verifyAuthorization(@RequestHeader("Authorization") String authorization) {
        if (authorization == null) {
            return ResponseEntity.badRequest().build();
        } else {
            String authorized = this.authorizationService.validateToken(authorization);
            return authorized != null ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
        }
    }

    @PostMapping({"/register"})
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        Optional<UsuarioModel> usuarioCadastrado = this.usuarioRepository.findByEmail(body.email());
        if (usuarioCadastrado.isEmpty()) {
            UsuarioModel usuario = new UsuarioModel(body.name(), body.email(), this.passwordEncoder.encode(body.senha()), body.dataNascimento(), body.enderecos(), body.perfis());
            this.usuarioRepository.save(usuario);
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
