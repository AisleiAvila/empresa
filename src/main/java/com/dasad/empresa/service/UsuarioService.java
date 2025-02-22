package com.dasad.empresa.service;

import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.UsuarioRequest;
import com.dasad.empresa.repository.EnderecoRepository;
import com.dasad.empresa.repository.UsuarioRecuperarSenhaRepository;
import com.dasad.empresa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioRecuperarSenhaRepository tokenRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

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
        enderecoRepository.findAll()
                .stream()
                .filter(endereco -> endereco.getUsuarioId().equals(id)).
                forEach(endereco -> enderecoRepository.deleteById(endereco.getId()));
        this.usuarioRepository.deleteById(id);
    }

    public void updatePassword(Integer id, String password) {
        this.usuarioRepository.updatePassword(id, password);
    }

}
