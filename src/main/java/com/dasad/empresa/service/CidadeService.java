package com.dasad.empresa.service;

import com.dasad.empresa.model.CidadeModel;
import com.dasad.empresa.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {
    @Autowired
    private CidadeRepository cidadeRepository;

    public CidadeService() {
    }

    public List<CidadeModel> findAll(String nome, Integer estadoId) {
        return this.cidadeRepository.findAll(nome, estadoId);
    }

    public Optional<CidadeModel> findById(Integer id) {
        return this.cidadeRepository.findById(id);
    }

}
