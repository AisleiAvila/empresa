package com.dasad.empresa.dto;

public record LoginRequestDTO(String email, String senha) {
    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String email() {
        return this.email;
    }

    public String senha() {
        return this.senha;
    }
}
