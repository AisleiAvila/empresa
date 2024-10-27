package com.dasad.empresa.repository;

import com.dasad.empresa.model.EnderecoModel;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository {
    List<EnderecoModel> findAll();

    Optional<EnderecoModel> findById(Integer id);

    EnderecoModel save(EnderecoModel endereco);

    void deleteById(Integer id);
}
