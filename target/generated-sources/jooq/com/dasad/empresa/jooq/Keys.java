/*
 * This file is generated by jOOQ.
 */
package com.dasad.empresa.jooq;


import com.dasad.empresa.jooq.tables.Endereco;
import com.dasad.empresa.jooq.tables.Perfil;
import com.dasad.empresa.jooq.tables.UnidadeFederativa;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.jooq.tables.UsuarioPerfil;
import com.dasad.empresa.jooq.tables.UsuarioRecuperarSenha;
import com.dasad.empresa.jooq.tables.records.EnderecoRecord;
import com.dasad.empresa.jooq.tables.records.PerfilRecord;
import com.dasad.empresa.jooq.tables.records.UnidadeFederativaRecord;
import com.dasad.empresa.jooq.tables.records.UsuarioPerfilRecord;
import com.dasad.empresa.jooq.tables.records.UsuarioRecord;
import com.dasad.empresa.jooq.tables.records.UsuarioRecuperarSenhaRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<EnderecoRecord> ENDERECO_PKEY = Internal.createUniqueKey(Endereco.ENDERECO, DSL.name("endereco_pkey"), new TableField[] { Endereco.ENDERECO.ID }, true);
    public static final UniqueKey<PerfilRecord> PERFIS_PKEY = Internal.createUniqueKey(Perfil.PERFIL, DSL.name("perfis_pkey"), new TableField[] { Perfil.PERFIL.ID }, true);
    public static final UniqueKey<UnidadeFederativaRecord> UNIDADE_FEDERATIVA_PKEY = Internal.createUniqueKey(UnidadeFederativa.UNIDADE_FEDERATIVA, DSL.name("unidade_federativa_pkey"), new TableField[] { UnidadeFederativa.UNIDADE_FEDERATIVA.ID }, true);
    public static final UniqueKey<UsuarioRecord> USUARIO_PKEY = Internal.createUniqueKey(Usuario.USUARIO, DSL.name("usuario_pkey"), new TableField[] { Usuario.USUARIO.ID }, true);
    public static final UniqueKey<UsuarioPerfilRecord> USUARIOS_PERFIS_PKEY = Internal.createUniqueKey(UsuarioPerfil.USUARIO_PERFIL, DSL.name("usuarios_perfis_pkey"), new TableField[] { UsuarioPerfil.USUARIO_PERFIL.USUARIO_ID, UsuarioPerfil.USUARIO_PERFIL.PERFIL_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<EnderecoRecord, UsuarioRecord> ENDERECO__FKEKDPB8K6GMP3LLLLA9D1QGMXK = Internal.createForeignKey(Endereco.ENDERECO, DSL.name("fkekdpb8k6gmp3lllla9d1qgmxk"), new TableField[] { Endereco.ENDERECO.USUARIO_ID }, Keys.USUARIO_PKEY, new TableField[] { Usuario.USUARIO.ID }, true);
    public static final ForeignKey<EnderecoRecord, UnidadeFederativaRecord> ENDERECO__FKSBFB2MDC0HMIXWPUD2D8O8QOY = Internal.createForeignKey(Endereco.ENDERECO, DSL.name("fksbfb2mdc0hmixwpud2d8o8qoy"), new TableField[] { Endereco.ENDERECO.UNIDADE_FEDERATIVA_ID }, Keys.UNIDADE_FEDERATIVA_PKEY, new TableField[] { UnidadeFederativa.UNIDADE_FEDERATIVA.ID }, true);
    public static final ForeignKey<UsuarioPerfilRecord, PerfilRecord> USUARIO_PERFIL__USUARIOS_PERFIS_PERFIL_ID_FKEY = Internal.createForeignKey(UsuarioPerfil.USUARIO_PERFIL, DSL.name("usuarios_perfis_perfil_id_fkey"), new TableField[] { UsuarioPerfil.USUARIO_PERFIL.PERFIL_ID }, Keys.PERFIS_PKEY, new TableField[] { Perfil.PERFIL.ID }, true);
    public static final ForeignKey<UsuarioPerfilRecord, UsuarioRecord> USUARIO_PERFIL__USUARIOS_PERFIS_USUARIO_ID_FKEY = Internal.createForeignKey(UsuarioPerfil.USUARIO_PERFIL, DSL.name("usuarios_perfis_usuario_id_fkey"), new TableField[] { UsuarioPerfil.USUARIO_PERFIL.USUARIO_ID }, Keys.USUARIO_PKEY, new TableField[] { Usuario.USUARIO.ID }, true);
    public static final ForeignKey<UsuarioRecuperarSenhaRecord, UsuarioRecord> USUARIO_RECUPERAR_SENHA__USUARIO_ID_FKEY = Internal.createForeignKey(UsuarioRecuperarSenha.USUARIO_RECUPERAR_SENHA, DSL.name("usuario_id_fkey"), new TableField[] { UsuarioRecuperarSenha.USUARIO_RECUPERAR_SENHA.USUARIO_ID }, Keys.USUARIO_PKEY, new TableField[] { Usuario.USUARIO.ID }, true);
}
