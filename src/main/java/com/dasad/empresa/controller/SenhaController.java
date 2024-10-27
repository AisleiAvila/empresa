package com.dasad.empresa.controller;

import com.dasad.empresa.api.SenhaApi;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.jooq.tables.records.UsuarioRecord;
import com.dasad.empresa.model.RecuperarSenha200Response;
import com.dasad.empresa.model.RecuperarSenhaRequest;
import com.dasad.empresa.service.EmailService;
import com.dasad.empresa.service.SenhaService;
import com.dasad.empresa.service.UsuarioService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/senha")
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class SenhaController implements SenhaApi {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private SenhaService senhaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthorizationService authorizationService;

    private static final Logger log = LogManager.getLogger(SenhaController.class);

//    @PostMapping("/lembrar-senha")
//    public ResponseEntity<String> lembrarSenha(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        UsuarioModel usuarioModel = usuarioService.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//
//        String token = UUID.randomUUID().toString();
//        usuarioService.createPasswordResetTokenForUser(convertToUsuario(usuarioModel), token);
//
//        String resetUrl = "http://localhost:8080/resetar-senha?token=" + token;
//        emailService.sendPasswordResetEmail(usuarioModel.getEmail(), resetUrl);
//
//        return ResponseEntity.ok("E-mail de redefinição de senha enviado com sucesso");
//    }

    private UsuarioRecord convertToUsuario(com.dasad.empresa.model.UsuarioModel usuarioModel) {
        UsuarioRecord usuarioRecord = new UsuarioRecord();
        usuarioRecord.setId(usuarioModel.getId());
        usuarioRecord.setNome(usuarioModel.getNome());
        usuarioRecord.setEmail(usuarioModel.getEmail());
        // Adicione outros campos conforme necessário
        return usuarioRecord;
    }

    @Override
    @PostMapping("/recuperar")
    public ResponseEntity<RecuperarSenha200Response> recuperarSenha(RecuperarSenhaRequest recuperarSenhaRequest) {
        String email = recuperarSenhaRequest.getEmail();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body(null);
        }

        var usuario = usuarioService.findByEmail(email).orElseThrow(() -> new RuntimeException("Email não encontrado"));

        String token = authorizationService.generateToken(usuario);
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);

        senhaService.create(usuario, token, expiryDate);

        String recoveryLink = "http://seu-dominio.com/reset-password?token=" + token;
        try {
            emailService.sendPasswordResetEmail(email, recoveryLink, usuario.getNome());
        } catch (MessagingException e) {
            log.error("Erro ao enviar e-mail de recuperação de senha para {}", email, e);
            throw new RuntimeException(e);
        }

        var response = new RecuperarSenha200Response();
        response.message("E-mail de recuperação de senha enviado com sucesso");

        return ResponseEntity.ok(response);
    }
}