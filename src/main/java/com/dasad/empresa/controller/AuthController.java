package com.dasad.empresa.controller;

import com.dasad.empresa.api.AuthApi;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.model.LoginRequestDTO;
import com.dasad.empresa.model.LoginResponseDTO;
import com.dasad.empresa.model.RegisterRequestDTO;
import com.dasad.empresa.model.RevokeToken200Response;
import com.dasad.empresa.model.RevokeTokenRequest;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@Log4j2
@RequestMapping({"/auth"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080", "http://localhost:8100"}
)
public class AuthController implements AuthApi {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    @Operation(summary = "Verify authorization endpoint")
    @GetMapping({"/verify-authorization"})
    public ResponseEntity<Boolean> verifyAuthorization(@RequestHeader("Authorization") String authorization) {
        log.info("Verify authorization endpoint");
        String authorized = this.authorizationService.validateToken(authorization);
        return authorized != null ? ResponseEntity.ok(true) : ResponseEntity.badRequest().build();

    }

    public AuthController(final UsuarioRepository usuarioRepository, final PasswordEncoder passwordEncoder, final AuthorizationService authorizationService) {
        log.info("AuthController constructor");
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    @Override
    @Operation(summary = "Login endpoint")
    @PostMapping({"/login"})
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        log.info("Login endpoint");
        Optional<UsuarioModel> optionalUsuario = this.usuarioRepository.findByEmail(loginRequestDTO.getEmail());
        if (optionalUsuario.isPresent()) {
            var usuario = optionalUsuario.get();
            if (this.passwordEncoder.matches(loginRequestDTO.getSenha(), usuario.getSenha())) {
                String authorization = this.authorizationService.generateToken(usuario);
                var loginResponseDTO  =  new LoginResponseDTO();
                loginResponseDTO.setNome(usuario.getNome());
                loginResponseDTO.setAuthorization(authorization);
                loginResponseDTO.setPerfil(usuario.getPerfis().get(0).getNome());
                return ResponseEntity.ok(loginResponseDTO);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @Override
    @Operation(summary = "Register endpoint")
    @PostMapping({"/register"})
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        log.info("Register endpoint");
        Optional<UsuarioModel> usuarioCadastrado = this.usuarioRepository.findByEmail(registerRequestDTO.getEmail());
        if (usuarioCadastrado.isEmpty()) {
            log.info("Usuário não cadastrado");
            var usuario = new UsuarioModel();
            usuario.setNome(registerRequestDTO.getNome());
            usuario.setEmail(registerRequestDTO.getEmail());
            usuario.setSenha(this.passwordEncoder.encode(registerRequestDTO.getSenha()));
//            usuario.setDataNascimento(convertLocalDateToString(registerRequestDTO.getDataNascimento()));
            usuario.setDataNascimento(registerRequestDTO.getDataNascimento());
            usuario.setEnderecos(registerRequestDTO.getEnderecos());
            usuario.setPerfis(registerRequestDTO.getPerfis());

            log.info("Criando usuário");
            this.usuarioRepository.create(usuario);
            log.info("Usuário criado");
            String authorization = this.authorizationService.generateToken(usuario);
            var loginResponseDTO  =  new LoginResponseDTO();
            loginResponseDTO.setNome(usuario.getNome());
            loginResponseDTO.setAuthorization(authorization);
            loginResponseDTO.setPerfil(usuario.getPerfis().get(0).getNome());
            return ResponseEntity.ok(loginResponseDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @Override
    @Operation(summary = "Revoke token")
    @PostMapping({"/revoke"})
    public ResponseEntity<RevokeToken200Response> revokeToken(RevokeTokenRequest revokeTokenRequest) {
        log.info("Revoke token");

        String authorization = revokeTokenRequest.getToken();
        if (authorization == null || authorization.isEmpty()) {
            throw new IllegalArgumentException("O token não pode ser nulo ou vazio");
        }
        authorizationService.revokeToken(authorization);
        return ResponseEntity.ok().build();
    }

}
