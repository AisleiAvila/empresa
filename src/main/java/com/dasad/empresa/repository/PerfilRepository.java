package com.dasad.empresa.repository;

import com.dasad.empresa.model.PerfilModel;

import java.util.List;

public interface PerfilRepository {
    List<PerfilModel> findAll();
}
