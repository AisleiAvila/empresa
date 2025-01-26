package com.dasad.empresa.controller;

import com.dasad.empresa.api.Endereco2Api;
import com.dasad.empresa.model.Endereco2Model;
import com.dasad.empresa.service.Endereco2Service;
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
@RequestMapping({"/endereco2"})
public class Endereco2Controller implements Endereco2Api {
    @Autowired
    private Endereco2Service enderecoService;

    public Endereco2Controller() {
    }

    @Override
    @PutMapping({"/{id}"})
    public ResponseEntity<Endereco2Model> updateEndereco2(@PathVariable Integer id, @RequestBody Endereco2Model enderecoDetails) {
//        return  null;
        return this.enderecoService.findById(id).map((endereco) -> {
            endereco.setLogradouro(enderecoDetails.getLogradouro());
//            endereco.setCidade(enderecoDetails.getCidade());
//            endereco.setEstado(enderecoDetails.getEstado());
            endereco.setCep(enderecoDetails.getCep());
            Endereco2Model enderecoSave = this.enderecoService.save(endereco);
            return ResponseEntity.ok(enderecoSave);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteEndereco2(@PathVariable Integer id) {
        if (this.enderecoService.findById(id).isPresent()) {
            this.enderecoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<Endereco2Model> createEndereco2(@RequestBody Endereco2Model endereco2Model) {
        return null;
//        var endereco = this.enderecoService.save(enderecoModel);
//        return ResponseEntity.ok(endereco);
    }

    @Override
    @GetMapping({"/{id}"})
    public ResponseEntity<Endereco2Model> detailEndereco2(Integer id) {
        return this.enderecoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Endereco2Model>> findEndereco2() {
        var enderecos =  this.enderecoService.findAll();
        return ResponseEntity.ok(enderecos);
    }
}
