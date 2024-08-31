package com.dasad.empresa.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

public class UsuarioModel implements Serializable {
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
    private Set<EnderecoModel> enderecos;

    public UsuarioModel(Integer id, String nome, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
    }

    public UsuarioModel(Integer id, String nome, String email, String senha, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public UsuarioModel(String nome, String email, String senha, LocalDate dataNascimento, Set<EnderecoModel> enderecos) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.enderecos = enderecos;
    }

    public Integer getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getSenha() {
        return this.senha;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Set<EnderecoModel> getEnderecos() {
        return this.enderecos;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setSenha(final String senha) {
        this.senha = senha;
    }

    public void setDataNascimento(final LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setEnderecos(final Set<EnderecoModel> enderecos) {
        this.enderecos = enderecos;
    }

    public UsuarioModel() {
    }
}
