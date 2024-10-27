package com.dasad.empresa.controller;

import com.dasad.empresa.api.UnidadeFederativaApi;
import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.model.UnidadeFederativaModel;
import com.dasad.empresa.model.UnidadeFederativaRequest;
import com.dasad.empresa.model.UnidadeFederativaResponseDTO;
import com.dasad.empresa.service.UnidadeFederativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/unidade-federativa"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class UnidadeFederativaController implements UnidadeFederativaApi {
    @Autowired
    private UnidadeFederativaService unidadeFederativaService;
    @Autowired
    private AuthorizationService authorizationService;

    public UnidadeFederativaController() {
    }

    @Override
    @PostMapping("/find")
    @PreAuthorize("hasAnyRole('Administrador', 'Moderador', 'Usuário')")
    public ResponseEntity<UnidadeFederativaResponseDTO> findUF(@RequestBody UnidadeFederativaRequest unidadeFederativaRequest) {
        try {
            Optional<List<UnidadeFederativaModel>> result = this.unidadeFederativaService.find(unidadeFederativaRequest);
            UnidadeFederativaResponseDTO responseDTO = new UnidadeFederativaResponseDTO();
            responseDTO.setUfs(result.orElse(Collections.emptyList()));
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
