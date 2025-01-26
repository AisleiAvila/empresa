package com.dasad.empresa.controller;

import com.dasad.empresa.api.ProdutoApi;
import com.dasad.empresa.model.ProdutoRequest;
import com.dasad.empresa.model.ProdutoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produto")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://localhost:8100"})
public class ProdutoController  implements ProdutoApi {

    @Override
    @PreAuthorize("hasAnyRole('Administrador', 'Moderador', 'Usuário')")
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> findProduto(ProdutoRequest produtoRequest) {
        return null;
    }
}
