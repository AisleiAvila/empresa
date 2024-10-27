package com.dasad.empresa.service;

import com.dasad.empresa.model.UnidadeFederativaModel;
import com.dasad.empresa.model.UnidadeFederativaRequest;
import com.dasad.empresa.repository.UnidadeFederativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeFederativaService {
    @Autowired
    private UnidadeFederativaRepository unidadeFederativaRepository;

    public UnidadeFederativaService() {
    }

    public Optional<List<UnidadeFederativaModel>> find(UnidadeFederativaRequest unidadeFederativaRequest) {
        return this.unidadeFederativaRepository.find(unidadeFederativaRequest);
    }

}
