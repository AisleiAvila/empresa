package com.dasad.empresa.controller;

import com.dasad.empresa.infra.security.AuthorizationService;
import com.dasad.empresa.models.UnidadeFederativaModel;
import com.dasad.empresa.models.request.UnidadeFederativaRequest;
import com.dasad.empresa.service.UnidadeFederativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping({"/unidadesFederativas"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080"}
)
public class UnidadeFederativaController {
    @Autowired
    private UnidadeFederativaService unidadeFederativaService;
    @Autowired
    private AuthorizationService authorizationService;

    public UnidadeFederativaController() {
    }

    @GetMapping
    @CrossOrigin(
            origins = {"http://localhost:4200", "http://localhost:8080"}
    )
    public CompletableFuture<ResponseEntity<List<UnidadeFederativaModel>>> getUnidadeFederativa(@RequestHeader("Authorization") String authorization, @RequestParam(value = "id",required = false) Integer id, @RequestParam(value = "nome",required = false) String nome, @RequestParam(value = "sigla",required = false) String sigla) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            String userEmail = this.authorizationService.validateToken(token);
            if (userEmail == null) {
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
            } else {
                UnidadeFederativaRequest unidadeFederativaRequest = UnidadeFederativaRequest.Builder.aUnidadeFederativaRequest().withNome(nome).withSigla(sigla).build();
                return CompletableFuture.supplyAsync(() -> {
                    List<UnidadeFederativaModel> result = this.unidadeFederativaService.findByNome(unidadeFederativaRequest);
                    return ResponseEntity.ok(result.isEmpty() ? Collections.emptyList() : result);
                });
            }
        } else {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
    }

    @PostMapping
    public UnidadeFederativaModel createUnidadeFederativa(@RequestBody UnidadeFederativaModel unidadeFederativa) {
        return this.unidadeFederativaService.save(unidadeFederativa);
    }
}
