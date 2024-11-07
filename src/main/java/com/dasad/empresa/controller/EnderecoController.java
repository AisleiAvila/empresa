package com.dasad.empresa.controller;

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
@RequestMapping({"/enderecos"})
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;

    public EnderecoController() {
    }

    @GetMapping
    public List<EnderecoModel> findAll() {
        return this.enderecoService.findAll();
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<EnderecoModel> getEnderecoById(@PathVariable Integer id) {
        return this.enderecoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EnderecoModel createEndereco(@RequestBody EnderecoModel endereco) {
        return this.enderecoService.save(endereco);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<EnderecoModel> updateEndereco(@PathVariable Integer id, @RequestBody EnderecoModel enderecoDetails) {
        return this.enderecoService.findById(id).map((endereco) -> {
            endereco.setLogradouro(enderecoDetails.getLogradouro());
            endereco.setCidade(enderecoDetails.getCidade());
//            endereco.setEstado(enderecoDetails.getEstado());
            endereco.setCep(enderecoDetails.getCep());
            EnderecoModel enderecoSave = this.enderecoService.save(endereco);
            return ResponseEntity.ok(enderecoSave);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> deleteEndereco(@PathVariable Integer id) {
        if (this.enderecoService.findById(id).isPresent()) {
            this.enderecoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
