package com.dasad.empresa.dto;

import com.dasad.empresa.models.EnderecoModel;
import com.dasad.empresa.models.PerfilModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record RegisterRequestDTO(String nome, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos, @JsonDeserialize(as = HashSet.class) Set<PerfilModel> perfis) {
    public RegisterRequestDTO(String nome, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos, Set<PerfilModel> perfis) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
        this.perfis = perfis;
    }

    public String nome() {
        return this.nome;
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