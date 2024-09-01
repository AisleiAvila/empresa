package com.dasad.empresa.dto;

import com.dasad.empresa.models.EnderecoModel;

import java.util.Set;

public record UnidadeFederativaDto(String sigla, String nome, Set<EnderecoModel> enderecos) {
    public UnidadeFederativaDto(String sigla, String nome, Set<EnderecoModel> enderecos) {
        this.sigla = sigla;
        this.nome = nome;
        this.enderecos = enderecos;
    }

    public String sigla() {
        return this.sigla;
    }

    public String nome() {
        return this.nome;
    }

    public Set<EnderecoModel> enderecos() {
        return this.enderecos;
    }
}
