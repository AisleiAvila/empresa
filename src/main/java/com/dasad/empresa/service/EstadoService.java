package com.dasad.empresa.service;

import com.dasad.empresa.model.EstadoModel;
import com.dasad.empresa.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {
    @Autowired
    private EstadoRepository estadoRepository;

    public EstadoService() {
    }

    public List<EstadoModel> findAll(String nome, Integer paisId) {
        return this.estadoRepository.findAll(nome, paisId);
    }

    public Optional<EstadoModel> findById(Integer id) {
        return this.estadoRepository.findById(id);
    }

}
