package com.dasad.empresa.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
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

}
