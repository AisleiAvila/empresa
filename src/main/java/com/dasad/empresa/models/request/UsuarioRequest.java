package com.dasad.empresa.models.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UsuarioRequest {
    private Integer id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private  Integer limit;
    private Integer offset;

    public UsuarioRequest() {
    }

    public UsuarioRequest(Integer id, String nome, String email, LocalDate dataNascimento, Integer limit, Integer offset) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.limit = limit;
        this.offset = offset;
    }

    public static final class Builder {
        private Integer id;
        private String nome;
        private String email;
        private LocalDate dataNascimento;
        private Integer limit;
        private Integer offset;

        public Builder() {
        }

        public static Builder aUnidadeFederativaRequest() {
            return new Builder();
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withDataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = dataNascimento;
            return this;
        }

        public Builder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public UsuarioRequest build() {
            return new UsuarioRequest(this.id, this.nome, this.email, this.dataNascimento, this.limit, this.offset);
        }
    }
}
