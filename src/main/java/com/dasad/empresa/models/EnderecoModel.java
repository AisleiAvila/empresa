package com.dasad.empresa.models;

import java.io.Serializable;

public class EnderecoModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private UsuarioModel usuario;
    private UnidadeFederativaModel unidadeFederativaModel;

    public EnderecoModel(Integer id, String logradouro, String bairro, String cidade, String estado, String cep, UsuarioModel usuario, UnidadeFederativaModel unidadeFederativaModel) {
        this.id = id;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.usuario = usuario;
        this.unidadeFederativaModel = unidadeFederativaModel;
    }

    public Integer getId() {
        return this.id;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public String getBairro() {
        return this.bairro;
    }

    public String getCidade() {
        return this.cidade;
    }

    public String getEstado() {
        return this.estado;
    }

    public String getCep() {
        return this.cep;
    }

    public UsuarioModel getUsuario() {
        return this.usuario;
    }

    public UnidadeFederativaModel getUnidadeFederativaModel() {
        return this.unidadeFederativaModel;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setLogradouro(final String logradouro) {
        this.logradouro = logradouro;
    }

    public void setBairro(final String bairro) {
        this.bairro = bairro;
    }

    public void setCidade(final String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public void setCep(final String cep) {
        this.cep = cep;
    }

    public void setUsuario(final UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public void setUnidadeFederativaModel(final UnidadeFederativaModel unidadeFederativaModel) {
        this.unidadeFederativaModel = unidadeFederativaModel;
    }
}
