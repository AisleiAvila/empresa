package com.dasad.empresa.service;

import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.jooq.tables.records.UsuarioRecuperarSenhaRecord;
import com.dasad.empresa.model.RecuperarSenha200Response;
import com.dasad.empresa.model.RecuperarSenhaRequest;
import com.dasad.empresa.model.SalvarSenha200Response;
import com.dasad.empresa.model.SalvarSenhaRequest;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.ValidarResetToken200Response;
import com.dasad.empresa.repository.SenhaRepository;
import com.dasad.empresa.repository.UsuarioRecuperarSenhaRepository;
import com.dasad.empresa.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SenhaService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SenhaRepository senhaRepository;

    @Autowired
    private UsuarioRecuperarSenhaRepository tokenRepository;

    private static final Logger log = LogManager.getLogger(SenhaService.class);

    public SenhaService() {
    }

    public void create(UsuarioModel usuarioModel, String token, LocalDateTime expiryDate) {
        try {
            UsuarioRecuperarSenhaRecord usuarioRecuperarSenhaRecord = new UsuarioRecuperarSenhaRecord();
            usuarioRecuperarSenhaRecord.setUsuarioId(usuarioModel.getId());
            usuarioRecuperarSenhaRecord.setToken(token);
            usuarioRecuperarSenhaRecord.setDataExpiracao(expiryDate);
            this.senhaRepository.create(usuarioRecuperarSenhaRecord);
        } catch (Exception e) {
            log.error("Erro ao criar token de recuperação de senha para o usuário: {}", usuarioModel.getId(), e);
            throw new RuntimeException("Falha ao criar token de recuperação de senha", e);
        }
    }

//    public void createPasswordResetTokenForUser(UsuarioRecord usuario, String token) {
//        PasswordResetTokenRecord tokenRecord = new PasswordResetTokenRecord();
//        tokenRecord.setToken(token);
//        tokenRecord.setUsuarioId(Long.valueOf(usuario.getId())); // Use o método correto para obter o ID do usuário
//        tokenRecord.setExpiryDate(new Timestamp(System.currentTimeMillis() + 3600000).toLocalDateTime()); // 1 hour expiry
//        tokenRepository.save(tokenRecord);
//    }
//
//    public
//
//    public void saveRecoveryToken(Integer id,  String token, Timestamp expiryDate) {
//
//        tokenRepository.save(tokenRecord);
//    }

    public ResponseEntity<RecuperarSenha200Response> getRecuperarSenha(RecuperarSenhaRequest recuperarSenhaRequest) {
        String email = recuperarSenhaRequest.getEmail();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body(null);
        }

        var usuario = usuarioService.findByEmail(email).orElseThrow(() -> new RuntimeException("Email não encontrado"));

        String token = authorizationService.generateToken(usuario);
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);

        create(usuario, token, expiryDate);

        String recoveryLink = "http://localhost:4200/senha/validar-reset-token?token=" + token;
        String emailContent = "Olá " + usuario.getNome() + ",\n\n" +
                "Recebemos sua solicitação para redefinir a senha de acesso ao Sistema Empresa.\n\n" +
                "Para prosseguir, clique no botão abaixo:\n\n" +
                "<a href=\"" + recoveryLink + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">Redefinir Senha</a>\n\n" +
                "Caso não consiga clicar no botão acima, copie e cole este endereço no seu navegador: " + recoveryLink + "\n\n" +
                "Atenciosamente,\n" +
                "Equipe Suporte do Sistema Empresa.";

        try {
            emailService.sendPasswordResetEmail(email, emailContent, usuario.getNome());
        } catch (MessagingException e) {
            log.error("Erro ao enviar e-mail de recuperação de senha para {}", email, e);
            throw new RuntimeException(e);
        }

        var response = new RecuperarSenha200Response();
        response.message("E-mail de recuperação de senha enviado com sucesso");

        return ResponseEntity.ok(response);
    }

    public  ResponseEntity<ValidarResetToken200Response> getValidarResetToken(String token) {
        try {
            String email = authorizationService.validateToken(token);
            if (email != null) {
                var usuarioModel = usuarioService.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                var response = new ValidarResetToken200Response();
                response.email(email);
                response.nome(usuarioModel.getNome());
                response.id(usuarioModel.getId());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public  ResponseEntity<SalvarSenha200Response> getSalvarSenha(SalvarSenhaRequest salvarSenhaRequest) {
        var response = new SalvarSenha200Response();
        try {
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



}
