package com.dasad.empresa.service;

import com.dasad.empresa.model.PaisModel;
import com.dasad.empresa.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaisService {
    @Autowired
    private PaisRepository paisRepository;

    public PaisService() {
    }

    public List<PaisModel> findAll(String nome) {
        return this.paisRepository.findAll(nome);
    }

    public Optional<PaisModel> findById(Integer id) {
        return this.paisRepository.findById(id);
    }

}
