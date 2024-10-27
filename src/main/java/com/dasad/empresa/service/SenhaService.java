package com.dasad.empresa.service;

import com.dasad.empresa.jooq.tables.records.UsuarioRecuperarSenhaRecord;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.repository.SenhaRepository;
import com.dasad.empresa.repository.UsuarioRecuperarSenhaRepository;
import com.dasad.empresa.repository.UsuarioRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SenhaService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SenhaRepository senhaRepository;

    @Autowired
    private UsuarioRecuperarSenhaRepository tokenRepository;

    private static final Logger log = LogManager.getLogger(SenhaService.class);

    public SenhaService() {
    }

    public void create(UsuarioModel usuario, String token, LocalDateTime expiryDate) {
        try {
            UsuarioRecuperarSenhaRecord usuarioRecuperarSenhaRecord = new UsuarioRecuperarSenhaRecord();
            usuarioRecuperarSenhaRecord.setUsuarioId(usuario.getId());
            usuarioRecuperarSenhaRecord.setToken(token);
            usuarioRecuperarSenhaRecord.setDataExpiracao(expiryDate);
            this.senhaRepository.create(usuarioRecuperarSenhaRecord);
        } catch (Exception e) {
            log.error("Erro ao criar token de recuperação de senha para o usuário: {}", usuario.getId(), e);
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

}
