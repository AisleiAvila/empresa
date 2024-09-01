package com.dasad.empresa.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilModel {
    private Integer id;
    private String nome;

    // Construtor sem argumentos
    public PerfilModel() {
    }

    // Construtor com argumentos
    public PerfilModel(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}