package com.dasad.empresa.dto;

import com.dasad.empresa.model.UsuarioModel;

import java.util.List;

public class UsuarioResponseDTO {
    private List<UsuarioModel> usuarios;
    private int totalRecords;

    public UsuarioResponseDTO(List<UsuarioModel> usuarios, int totalRecords) {
        this.usuarios = usuarios;
        this.totalRecords = totalRecords;
    }

    public List<UsuarioModel> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioModel> usuarios) {
        this.usuarios = usuarios;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}