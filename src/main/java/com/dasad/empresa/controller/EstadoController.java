package com.dasad.empresa.controller;

import com.dasad.empresa.api.EstadoApi;
import com.dasad.empresa.model.EstadoModel;
import com.dasad.empresa.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/estado"})
public class EstadoController implements EstadoApi {
    @Autowired
    private EstadoService estadoService;

    public EstadoController() {
    }

    @Override
    @GetMapping({"/{id}"})
    public ResponseEntity<EstadoModel> detailEstado(Integer id) {
        return this.estadoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EstadoModel>> findEstado(@RequestParam(required = false) String nome, @RequestParam(required = false) Integer pais_id) {
        var estados =  this.estadoService.findAll(nome, pais_id);
        return ResponseEntity.ok(estados);
    }

}
