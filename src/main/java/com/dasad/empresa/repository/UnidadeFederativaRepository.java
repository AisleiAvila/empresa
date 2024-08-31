package com.dasad.empresa.repository;

import com.dasad.empresa.models.UnidadeFederativaModel;
import com.dasad.empresa.models.request.UnidadeFederativaRequest;

import java.util.List;
import java.util.Optional;

public interface UnidadeFederativaRepository {
    Optional<List<UnidadeFederativaModel>> findByNome(UnidadeFederativaRequest unidadeFederativaRequest);

    UnidadeFederativaModel save(UnidadeFederativaModel unidadeFederativa);

    void deleteById(Integer id);
}
