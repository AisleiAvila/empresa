package com.dasad.empresa.dto;

import com.dasad.empresa.models.EnderecoModel;
import java.util.Set;

public record UsuarioDto(String nome, String dataNascimento, Set<EnderecoModel> enderecos) {
    public UsuarioDto(String nome, String dataNascimento, Set<EnderecoModel> enderecos) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
    }

    public String nome() {
        return this.nome;
    }

    public String dataNascimento() {
        return this.dataNascimento;
    }

    public Set<EnderecoModel> enderecos() {
        return this.enderecos;
    }
}
