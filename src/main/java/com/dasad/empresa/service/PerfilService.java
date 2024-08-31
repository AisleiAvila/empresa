package com.dasad.empresa.service;

import com.dasad.empresa.models.PerfilModel;
import com.dasad.empresa.repository.PerfilRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
