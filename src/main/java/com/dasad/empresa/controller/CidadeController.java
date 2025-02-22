package com.dasad.empresa.controller;

import com.dasad.empresa.api.CidadeApi;
import com.dasad.empresa.model.CidadeModel;
import com.dasad.empresa.service.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/cidade"})
public class CidadeController implements CidadeApi {
    @Autowired
    private CidadeService cidadeService;

    public CidadeController() {
    }

    @Override
    @GetMapping({"/{id}"})
    public ResponseEntity<CidadeModel> detailCidade(Integer id) {
        return this.cidadeService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CidadeModel>> findCidade(@RequestParam(required = false) String nome, @RequestParam(required = false) Integer estado_id) {
        var cidades =  this.cidadeService.findAll(nome, estado_id);
        return ResponseEntity.ok(cidades);
    }

}
