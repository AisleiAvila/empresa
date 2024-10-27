package com.dasad.empresa.repository;

import com.dasad.empresa.model.UnidadeFederativaModel;
import com.dasad.empresa.model.UnidadeFederativaRequest;

import java.util.List;
import java.util.Optional;

public interface UnidadeFederativaRepository {
    Optional<List<UnidadeFederativaModel>> find(UnidadeFederativaRequest unidadeFederativaRequest);
}
