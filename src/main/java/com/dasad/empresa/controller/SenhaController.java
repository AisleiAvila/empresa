package com.dasad.empresa.controller;

import com.dasad.empresa.api.SenhaApi;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.jooq.tables.records.UsuarioRecord;
import com.dasad.empresa.model.RecuperarSenha200Response;
import com.dasad.empresa.model.RecuperarSenhaRequest;
import com.dasad.empresa.model.SalvarSenha200Response;
import com.dasad.empresa.model.SalvarSenhaRequest;
import com.dasad.empresa.model.ValidarResetToken200Response;
import com.dasad.empresa.service.EmailService;
import com.dasad.empresa.service.SenhaService;
import com.dasad.empresa.service.UsuarioService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

        String recoveryLink = "http://localhost:4200/senha/validar-reset-token?token=" + token;
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

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<SalvarSenha200Response> salvarSenha(SalvarSenhaRequest salvarSenhaRequest) {
        var response = new SalvarSenha200Response();
        try {
            if (salvarSenhaRequest.getToken() == null)  {
                response.message("Token inválido ou expirado");
                return ResponseEntity.badRequest().body(response);
            }

            String email = authorizationService.validateToken(salvarSenhaRequest.getToken());

            if (email == null) {
                response.message("Token inválido ou expirado");
                return ResponseEntity.badRequest().body(response);
            }

            // TODO: Implementar a lógica para buscar o id do usuário pelo e-mail
            var usuario =  usuarioService.findByEmail(email);

            if (usuario.isEmpty()) {
                response.message("Usuário não encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            if (salvarSenhaRequest.getSenha() != salvarSenhaRequest.getRepeatSenha()) {
                response.message("As senhas não conferem");
                return ResponseEntity.badRequest().body(response);
            }

            usuarioService.updatePassword(usuario.get().getId(), salvarSenhaRequest.getSenha());
            response.message("Senha atualizada com sucesso");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.message("Erro ao atualizar senha");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    @GetMapping("/validar-reset-token")
    public ResponseEntity<ValidarResetToken200Response> validarResetToken(String token) {
        try {
            if (token != null)  {
                String email = authorizationService.validateToken(token);
                if (email != null) {
                    var usuario = usuarioService.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                    var response = new ValidarResetToken200Response();
                    response.email(email);
                    response.nome(usuario.getNome());
                    response.id(usuario.getId());
                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}