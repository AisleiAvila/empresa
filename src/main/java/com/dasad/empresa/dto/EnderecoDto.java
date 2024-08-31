package com.dasad.empresa.dto;

public record EnderecoDto(String logradouro, String bairro, String cidade, String estado, String cep) {
    public EnderecoDto(String logradouro, String bairro, String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public String logradouro() {
        return this.logradouro;
    }

    public String bairro() {
        return this.bairro;
    }

    public String cidade() {
        return this.cidade;
    }

    public String estado() {
        return this.estado;
    }

    public String cep() {
        return this.cep;
    }
}
