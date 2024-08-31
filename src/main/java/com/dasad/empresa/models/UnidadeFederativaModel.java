package com.dasad.empresa.models;

import java.io.Serializable;

public class UnidadeFederativaModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Integer id;
    private String nome;
    private String sigla;

    public UnidadeFederativaModel(Integer id, String nome, String sigla) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
    }

    public Integer getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getSigla() {
        return this.sigla;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public void setSigla(final String sigla) {
        this.sigla = sigla;
    }
}
