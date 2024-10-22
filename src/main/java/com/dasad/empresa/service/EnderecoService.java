package com.dasad.empresa.service;

import com.dasad.empresa.model.EnderecoModel;
import com.dasad.empresa.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

    public EnderecoService() {
    }

    public List<EnderecoModel> findAll() {
        return this.enderecoRepository.findAll();
    }

    public Optional<EnderecoModel> findById(Integer id) {
        return this.enderecoRepository.findById(id);
    }

    public EnderecoModel save(EnderecoModel endereco) {
        return this.enderecoRepository.save(endereco);
    }

    public void deleteById(Integer id) {
        this.enderecoRepository.deleteById(id);
    }
}
