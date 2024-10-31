package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.UsuarioRecuperarSenha;
import com.dasad.empresa.jooq.tables.records.UsuarioRecuperarSenhaRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRecuperarSenhaRepository {

    @Autowired
    private DSLContext dsl;

    public void save(UsuarioRecuperarSenhaRecord tokenRecord) {
        dsl.insertInto(UsuarioRecuperarSenha.USUARIO_RECUPERAR_SENHA)
                .set(tokenRecord)
                .execute();
    }
}