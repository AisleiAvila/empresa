package com.dasad.empresa.controller;

import com.dasad.empresa.api.OrganizacaoApi;
import com.dasad.empresa.model.OrganizacaoModel;
import com.dasad.empresa.model.OrganizacaoRequest;
import com.dasad.empresa.model.OrganizacaoRequestDTO;
import com.dasad.empresa.model.OrganizacaoResponseDTO;
import com.dasad.empresa.service.OrganizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizacao")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://localhost:8100"})
public class OrganizacaoController implements OrganizacaoApi {

    @Autowired
    private OrganizacaoService organizacaoService;

    public OrganizacaoController() {
    }

    @Override
    @PutMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<OrganizacaoModel> createOrganizacao(OrganizacaoRequestDTO organizacaoRequestDTO) {
        OrganizacaoModel organizacaoModel = new OrganizacaoModel();
        organizacaoModel.setId(organizacaoRequestDTO.getId());
        organizacaoModel.setNome(organizacaoRequestDTO.getNome());
        organizacaoModel.setNif(organizacaoRequestDTO.getNif());
        organizacaoModel.setEmail(organizacaoRequestDTO.getEmail());
        organizacaoModel.setWebsite(organizacaoRequestDTO.getWebsite());
        organizacaoModel.setorAtividade(organizacaoRequestDTO.getSetorAtividade());
        organizacaoModel.setMissao(organizacaoRequestDTO.getMissao());
        organizacaoModel.setRepresentanteLegal(organizacaoRequestDTO.getRepresentanteLegal());
        organizacaoModel.setCargo(organizacaoRequestDTO.getCargo());
        organizacaoModel.setNumeroRegistoComercial(organizacaoRequestDTO.getNumeroRegistoComercial());
        organizacaoModel.setDataRegisto(organizacaoRequestDTO.getDataRegisto());
        return ResponseEntity.ok(this.organizacaoService.create(organizacaoModel));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<Void> deleteOrganizacao(Integer id) {
        if (this.organizacaoService.findById(id).isPresent()) {
            this.organizacaoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('Administrador', 'Moderador', 'Usuário')")
    public ResponseEntity<OrganizacaoResponseDTO> detailOrganizacao(Integer id) {
        Optional<OrganizacaoModel> organizacao = this.organizacaoService.findById(id);
        return organizacao.map(u -> {
            OrganizacaoResponseDTO responseDTO = new OrganizacaoResponseDTO();
            responseDTO.setOrganizacoes(Collections.singletonList(organizacao.orElse(null)));
            return ResponseEntity.ok(responseDTO);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/find")
    @PreAuthorize("hasAnyRole('Administrador', 'Moderador', 'Usuário')")
    public ResponseEntity<OrganizacaoResponseDTO> findOrganizacao(OrganizacaoRequest organizacaoRequest) {
        try {
            Optional<List<OrganizacaoModel>> organizacoes = this.organizacaoService.find(organizacaoRequest);
            var totalRecords = 0;
            if (organizacaoRequest.getOffset() == null || organizacaoRequest.getOffset() == 0) {
                var retorno = this.organizacaoService.countTotalRecords(organizacaoRequest);
                totalRecords = retorno.orElse(0);
            }
            OrganizacaoResponseDTO responseDTO = new OrganizacaoResponseDTO();
            responseDTO.setOrganizacoes(organizacoes.orElse(Collections.emptyList()));
            responseDTO.setTotalRecords(totalRecords);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            throw new RuntimeException("Erro interno do servidor", e);
        }
    }

    @Override
    @PatchMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<OrganizacaoModel> updateOrganizacao(OrganizacaoModel organizacaoModel) {
        var organizacao = this.organizacaoService.update(organizacaoModel);
        return ResponseEntity.ok(organizacao);
    }
}
