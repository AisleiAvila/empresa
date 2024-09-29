package com.dasad.empresa.controller;

import com.dasad.empresa.jooq.tables.records.UsuarioRecord;
import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.service.EmailService;
import com.dasad.empresa.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class PasswordResetController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/lembrar-senha")
    public ResponseEntity<String> lembrarSenha(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        UsuarioModel usuarioModel = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = UUID.randomUUID().toString();
        usuarioService.createPasswordResetTokenForUser(convertToUsuario(usuarioModel), token);

        String resetUrl = "http://localhost:8080/resetar-senha?token=" + token;
        emailService.sendPasswordResetEmail(usuarioModel.getEmail(), resetUrl);

        return ResponseEntity.ok("E-mail de redefinição de senha enviado com sucesso");
    }

    private UsuarioRecord convertToUsuario(UsuarioModel usuarioModel) {
        UsuarioRecord usuarioRecord = new UsuarioRecord();
        usuarioRecord.setId(usuarioModel.getId());
        usuarioRecord.setNome(usuarioModel.getNome());
        usuarioRecord.setEmail(usuarioModel.getEmail());
        // Adicione outros campos conforme necessário
        return usuarioRecord;
    }
}