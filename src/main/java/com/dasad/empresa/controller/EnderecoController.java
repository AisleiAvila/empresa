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

    @Override
    @PostMapping
    public ResponseEntity<EnderecoModel> createEndereco(@RequestBody EnderecoModel enderecoModel) {
        var endereco = this.enderecoService.save(enderecoModel);
        return ResponseEntity.ok(endereco);
    }

    @Override
    @GetMapping({"/{id}"})
    public ResponseEntity<EnderecoModel> detailEndereco(Integer id) {
        return this.enderecoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EnderecoModel>> findEndereco() {
        var enderecos =  this.enderecoService.findAll();
        return ResponseEntity.ok(enderecos);
    }
}
