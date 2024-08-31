package com.dasad.empresa.dto;

public record LoginResponseDTO(String nome, String authorization) {
    public LoginResponseDTO(String nome, String authorization) {
        this.nome = nome;
        this.authorization = authorization;
    }

    public String nome() {
        return this.nome;
    }

    public String authorization() {
        return this.authorization;
    }
}
