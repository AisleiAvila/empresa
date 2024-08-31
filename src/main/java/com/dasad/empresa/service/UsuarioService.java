package com.dasad.empresa.service;

import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
    }

    public List<UsuarioModel> findAll() {
        return this.usuarioRepository.findAll();
    }

    public Optional<UsuarioModel> findById(Integer id) {
        return this.usuarioRepository.findById(id);
    }

    public UsuarioModel save(UsuarioModel usuario) {
        return this.usuarioRepository.save(usuario);
    }

    public void deleteById(Integer id) {
        this.usuarioRepository.deleteById(id);
    }
}
