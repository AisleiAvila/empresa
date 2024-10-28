package com.dasad.empresa.repository;

import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.UsuarioRequest;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<List<UsuarioModel>> find(UsuarioRequest usuariorequest);
    Optional<Integer> countTotalRecords(UsuarioRequest usuariorequest);
    Optional<UsuarioModel> findById(Integer id);
    Optional<UsuarioModel> findByEmail(String email);
    UsuarioModel create(UsuarioModel usuario);
    UsuarioModel update(UsuarioModel usuario);
    void deleteById(Integer id);
    void updatePassword(Integer id, String password);
}
