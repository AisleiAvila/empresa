package com.dasad.empresa.models;

import com.dasad.empresa.model.UsuarioModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class EnderecoModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    @JsonBackReference // Fictícia, não persistida
    private UsuarioModel usuario;

    public EnderecoModel(Integer id, String logradouro, String bairro, String cidade, String estado, String cep, UsuarioModel usuario) {
        this.id = id;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.usuario = usuario;
    }

}
