package com.dasad.empresa.repository;

import com.dasad.empresa.model.PaisModel;

import java.util.List;
import java.util.Optional;

public interface PaisRepository {
    List<PaisModel> findAll(String nome);

    Optional<PaisModel> findById(Integer id);
}
