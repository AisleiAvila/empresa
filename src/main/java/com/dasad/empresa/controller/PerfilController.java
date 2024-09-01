package com.dasad.empresa.controller;

import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.models.PerfilModel;
import com.dasad.empresa.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/perfis"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class PerfilController {
    @Autowired
    private PerfilService perfilService;
    @Autowired
    private AuthorizationService authorizationService;

    public PerfilController() {
    }

    @GetMapping
    public List<PerfilModel> findAll() {
        return this.perfilService.findAll();
    }
}
