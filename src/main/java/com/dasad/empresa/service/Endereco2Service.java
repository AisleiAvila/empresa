package com.dasad.empresa.service;

import com.dasad.empresa.model.Endereco2Model;
import com.dasad.empresa.repository.Endereco2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Endereco2Service {
    @Autowired
    private Endereco2Repository enderecoRepository;

    public Endereco2Service() {
    }

    public List<Endereco2Model> findAll() {
        return this.enderecoRepository.findAll();
    }

    public Optional<Endereco2Model> findById(Integer id) {
        return this.enderecoRepository.findById(id);
    }

    public Endereco2Model save(Endereco2Model endereco) {
        return this.enderecoRepository.save(endereco);
    }

    public void deleteById(Integer id) {
        this.enderecoRepository.deleteById(id);
    }
}
