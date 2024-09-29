package com.dasad.empresa.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UsuarioModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;

    @JsonManagedReference
    private Set<EnderecoModel> enderecos;

    @JsonManagedReference
    private Set<PerfilModel> perfis;

    public UsuarioModel(Integer id, String nome, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos, Set<PerfilModel> perfis) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
        this.perfis = perfis;
    }

    public UsuarioModel(Integer id, String nome, String email, String senha, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public UsuarioModel(String nome, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos, Set<PerfilModel> perfis) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
        this.perfis = perfis;
    }

    public UsuarioModel() {
    }
}
