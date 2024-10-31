package com.dasad.empresa.controller;

import com.dasad.empresa.dto.LoginRequestDTO;
import com.dasad.empresa.dto.LoginResponseDTO;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.model.RegisterRequestDTO;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
        log.info("Login endpoint");
        Optional<UsuarioModel> optionalUsuario = this.usuarioRepository.findByEmail(body.email());
        if (optionalUsuario.isPresent()) {
            var usuario = optionalUsuario.get();
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
        log.info("Verify authorization endpoint");
        if (authorization == null) {
            return ResponseEntity.badRequest().build();
        } else {
            String authorized = this.authorizationService.validateToken(authorization);
            return authorized != null ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Register endpoint")
    @PostMapping({"/register"})
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        log.info("Register endpoint");
        Optional<UsuarioModel> usuarioCadastrado = this.usuarioRepository.findByEmail(body.getEmail());
        if (usuarioCadastrado.isEmpty()) {
            log.info("Usuário não cadastrado");
            var usuario = new UsuarioModel();
            usuario.setNome(body.getNome());
            usuario.setEmail(body.getEmail());
            usuario.setSenha(this.passwordEncoder.encode(body.getSenha()));
            usuario.setDataNascimento(body.getDataNascimento());
            usuario.setEnderecos(body.getEnderecos());
            usuario.setPerfis(body.getPerfis());

            log.info("Criando usuário");
            this.usuarioRepository.create(usuario);
            log.info("Usuário criado");
            String authorization = this.authorizationService.generateToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(usuario.getNome(), authorization));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Revoke token")
    @PostMapping({"/revoke"})
    public ResponseEntity revoke(@RequestHeader("Authorization") String authorization) {
        log.info("evoke token");
        authorizationService.revokeToken(authorization);
            return ResponseEntity.ok().build();
    }

    public AuthController(final UsuarioRepository usuarioRepository, final PasswordEncoder passwordEncoder, final AuthorizationService authorizationService) {
        log.info("AuthController constructor");
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }
}
