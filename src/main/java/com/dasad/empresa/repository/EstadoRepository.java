package com.dasad.empresa.repository;

import com.dasad.empresa.model.EstadoModel;

import java.util.List;
import java.util.Optional;

public interface EstadoRepository {
    List<EstadoModel> findAll(String nome, Integer paisId);

    Optional<EstadoModel> findById(Integer id);
}
