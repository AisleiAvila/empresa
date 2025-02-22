package com.dasad.empresa.controller;

import com.dasad.empresa.api.EnderecoApi;
import com.dasad.empresa.model.EnderecoModel;
import com.dasad.empresa.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/endereco"})
public class EnderecoController implements EnderecoApi {
    @Autowired
    private EnderecoService enderecoService;

    public EnderecoController() {
    }

    @Override
    @PutMapping({"/{id}"})
    public ResponseEntity<EnderecoModel> updateendereco(@PathVariable Integer id, @RequestBody EnderecoModel enderecoDetails) {
        return this.enderecoService.findById(id).map((endereco) -> {
            endereco.setLogradouro(enderecoDetails.getLogradouro());
            endereco.setCidadeId(enderecoDetails.getCidadeId());
            endereco.setCep(enderecoDetails.getCep());
            endereco.setNumero(enderecoDetails.getNumero());
            endereco.setComplemento(enderecoDetails.getComplemento());
            endereco.setBairro(enderecoDetails.getBairro());
            EnderecoModel enderecoSave = this.enderecoService.save(endereco);

            return ResponseEntity.ok(enderecoSave);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteendereco(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        if (this.enderecoService.findById(id).isPresent()) {
            this.enderecoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<EnderecoModel> createendereco(@RequestBody EnderecoModel enderecoModel) {
        if (enderecoModel == null) {
            return ResponseEntity.badRequest().build();
        }

        var endereco = this.enderecoService.save(enderecoModel);
        return ResponseEntity.ok(endereco);
    }

    @Override
    @GetMapping({"/{id}"})
    public ResponseEntity<EnderecoModel> detailendereco(Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        return this.enderecoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EnderecoModel>> findendereco() {
        var enderecos =  this.enderecoService.findAll();
        return ResponseEntity.ok(enderecos);
    }
}
