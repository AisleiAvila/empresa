package com.dasad.empresa.controller;

import com.dasad.empresa.api.SenhaApi;
import com.dasad.empresa.model.RecuperarSenha200Response;
import com.dasad.empresa.model.RecuperarSenhaRequest;
import com.dasad.empresa.model.SalvarSenha200Response;
import com.dasad.empresa.model.SalvarSenhaRequest;
import com.dasad.empresa.model.ValidarResetToken200Response;
import com.dasad.empresa.service.SenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/senha")
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080", "http://localhost:8100"}
)
public class SenhaController implements SenhaApi {

    @Autowired
    private SenhaService senhaService;

    @Override
    @PostMapping("/recuperar")
    public ResponseEntity<RecuperarSenha200Response> recuperarSenha(RecuperarSenhaRequest recuperarSenhaRequest) {
        return senhaService.getRecuperarSenha(recuperarSenhaRequest);
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<SalvarSenha200Response> salvarSenha(SalvarSenhaRequest salvarSenhaRequest) {
        return senhaService.getSalvarSenha(salvarSenhaRequest);
    }


    @GetMapping("/validar-reset-token")
    @Override
    public ResponseEntity<ValidarResetToken200Response> validarResetToken(String token) {
        return senhaService.getValidarResetToken(token);
    }
}