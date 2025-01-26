package com.dasad.empresa.repository;

import com.dasad.empresa.model.CidadeModel;

import java.util.List;
import java.util.Optional;

public interface CidadeRepository {
    List<CidadeModel> findAll(String nome, Integer paisId);

    Optional<CidadeModel> findById(Integer id);
}
