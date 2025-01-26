package com.dasad.empresa.controller;

import com.dasad.empresa.api.PaisApi;
import com.dasad.empresa.model.PaisModel;
import com.dasad.empresa.service.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/pais"})
public class PaisController implements PaisApi {
    @Autowired
    private PaisService paisService;

    public PaisController() {
    }

    @Override
    @GetMapping({"/{id}"})
    public ResponseEntity<PaisModel> detailPais(Integer id) {
        return this.paisService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PaisModel>> findPais(@RequestParam(required = false) String nome) {
        var paises =  this.paisService.findAll(nome);
        return ResponseEntity.ok(paises);
    }

}
