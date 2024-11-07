package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.records.UsuarioRecuperarSenhaRecord;

public interface SenhaRepository {

    void create(UsuarioRecuperarSenhaRecord usuario);

}
