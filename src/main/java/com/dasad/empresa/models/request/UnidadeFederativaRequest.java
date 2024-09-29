package com.dasad.empresa.models.request;

import lombok.Getter;

import java.util.Optional;

public class UnidadeFederativaRequest {
    private Integer id;
    @Getter
    private String nome;
    private String sigla;

    public UnidadeFederativaRequest() {
    }

    public UnidadeFederativaRequest(Integer id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }

    public Optional<String> getSigla() {
        return Optional.ofNullable(this.sigla);
    }

    public Optional<Integer> getId() {
        return Optional.ofNullable(this.id);
    }

    public static final class Builder {
        private Integer id;
        private String nome;
        private String sigla;

        public Builder() {
        }

        public static Builder aUnidadeFederativaRequest() {
            return new Builder();
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder withSigla(String sigla) {
            this.sigla = sigla;
            return this;
        }

        public UnidadeFederativaRequest build() {
            return new UnidadeFederativaRequest(this.id, this.nome, this.sigla);
        }
    }
}
