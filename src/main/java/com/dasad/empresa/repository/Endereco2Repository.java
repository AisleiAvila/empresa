package com.dasad.empresa.repository;

import com.dasad.empresa.model.Endereco2Model;

import java.util.List;
import java.util.Optional;

public interface Endereco2Repository {
    List<Endereco2Model> findAll();

    Optional<Endereco2Model> findById(Integer id);

    Endereco2Model save(Endereco2Model endereco);

    void deleteById(Integer id);
}
