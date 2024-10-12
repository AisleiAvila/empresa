package com.dasad.empresa.service;

import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {
    @Autowired
    private PerfilRepository perfilRepository;

    public PerfilService() {
    }

    public List<PerfilModel> findAll() {
        return this.perfilRepository.findAll();
    }
}
