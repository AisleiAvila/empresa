package com.dasad.empresa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;


    @JsonBackReference // Fictícia, não persistida
    private UsuarioModel usuario;

    // Construtor sem argumentos
    public PerfilModel() {
    }

    // Construtor com argumentos
    @JsonCreator
    public PerfilModel(@JsonProperty("id") Integer id, @JsonProperty("nome") String nome) {
        this.id = id;
        this.nome = nome;
    }
}