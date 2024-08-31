package com.dasad.empresa.service;

import com.dasad.empresa.models.UnidadeFederativaModel;
import com.dasad.empresa.models.request.UnidadeFederativaRequest;
import com.dasad.empresa.repository.UnidadeFederativaRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnidadeFederativaService {
    @Autowired
    private UnidadeFederativaRepository unidadeFederativaRepository;

    public UnidadeFederativaService() {
    }

    public List<UnidadeFederativaModel> findByNome(UnidadeFederativaRequest unidadeFederativaRequest) {
        return (List)this.unidadeFederativaRepository.findByNome(unidadeFederativaRequest).orElse(Collections.emptyList());
    }

    public UnidadeFederativaModel save(UnidadeFederativaModel unidadeFederativa) {
        return this.unidadeFederativaRepository.save(unidadeFederativa);
    }

    public void deleteById(Integer id) {
        this.unidadeFederativaRepository.deleteById(id);
    }
}
