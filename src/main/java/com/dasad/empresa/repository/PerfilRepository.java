package com.dasad.empresa.repository;

import com.dasad.empresa.models.PerfilModel;
import java.util.List;

public interface PerfilRepository {
    List<PerfilModel> findAll();
}
