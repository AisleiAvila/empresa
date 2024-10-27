package com.dasad.empresa.service;

import com.dasad.empresa.jooq.tables.records.PasswordResetTokenRecord;
import com.dasad.empresa.jooq.tables.records.UsuarioRecord;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.UsuarioRequest;
import com.dasad.empresa.repository.PasswordResetTokenRepository;
import com.dasad.empresa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public UsuarioService() {
    }

    public Optional<List<UsuarioModel>> find(UsuarioRequest usuarioRequest) {
        return this.usuarioRepository.find(usuarioRequest);
    }

    public Optional<Integer> countTotalRecords(UsuarioRequest usuarioRequest) {
        return this.usuarioRepository.countTotalRecords(usuarioRequest);
    }

    public Optional<UsuarioModel> findById(Integer id) {
        return this.usuarioRepository.findById(id);
    }

    public Optional<com.dasad.empresa.model.UsuarioModel> findByEmail(String email) {
        return this.usuarioRepository.findByEmail(email);
    }

    public UsuarioModel create(UsuarioModel usuario) {
        return this.usuarioRepository.create(usuario);
    }

    public UsuarioModel update(UsuarioModel usuario) {
        return this.usuarioRepository.update(usuario);
    }

    public void deleteById(Integer id) {
        this.usuarioRepository.deleteById(id);
    }

    public void createPasswordResetTokenForUser(UsuarioRecord usuario, String token) {
        PasswordResetTokenRecord tokenRecord = new PasswordResetTokenRecord();
        tokenRecord.setToken(token);
        tokenRecord.setUsuarioId(Long.valueOf(usuario.getId())); // Use o método correto para obter o ID do usuário
        tokenRecord.setExpiryDate(new Timestamp(System.currentTimeMillis() + 3600000).toLocalDateTime()); // 1 hour expiry
        tokenRepository.save(tokenRecord);
    }

}
