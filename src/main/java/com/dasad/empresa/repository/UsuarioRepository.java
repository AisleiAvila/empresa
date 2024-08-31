package com.dasad.empresa.repository;

import com.dasad.empresa.models.UsuarioModel;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    List<UsuarioModel> findAll();

    Optional<UsuarioModel> findById(Integer id);

    UsuarioModel save(UsuarioModel usuario);

    void deleteById(Integer id);

    Optional<UsuarioModel> findByEmail(String login);
}
