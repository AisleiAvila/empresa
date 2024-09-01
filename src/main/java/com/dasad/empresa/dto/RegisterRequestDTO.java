package com.dasad.empresa.dto;

import com.dasad.empresa.models.EnderecoModel;
import com.dasad.empresa.models.PerfilModel;

import java.time.LocalDate;
import java.util.Set;

public record RegisterRequestDTO(String name, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos, Set<PerfilModel> perfis) {
    public RegisterRequestDTO(String name, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos, Set<PerfilModel> perfis) {
        this.name = name;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
        this.perfis = perfis;
    }

    public String name() {
        return this.name;
    }

    public String email() {
        return this.email;
    }

    public String senha() {
        return this.senha;
    }

    public LocalDate dataNascimento() {
        return this.dataNascimento;
    }

    public Set<EnderecoModel> enderecos() {
        return this.enderecos;
    }

    public Set<PerfilModel> perfis() {
        return this.perfis;
    }
}
