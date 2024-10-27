package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.records.UsuarioRecuperarSenhaRecord;
import com.dasad.empresa.model.UsuarioModel;

public interface SenhaRepository {

    void create(UsuarioRecuperarSenhaRecord usuario);

}
