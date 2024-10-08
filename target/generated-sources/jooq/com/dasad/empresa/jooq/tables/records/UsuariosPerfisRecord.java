/*
 * This file is generated by jOOQ.
 */
package com.dasad.empresa.jooq.tables.records;


import com.dasad.empresa.jooq.tables.UsuariosPerfis;

import org.jooq.Record2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class UsuariosPerfisRecord extends UpdatableRecordImpl<UsuariosPerfisRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.usuarios_perfis.usuario_id</code>.
     */
    public void setUsuarioId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.usuarios_perfis.usuario_id</code>.
     */
    public Integer getUsuarioId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.usuarios_perfis.perfil_id</code>.
     */
    public void setPerfilId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.usuarios_perfis.perfil_id</code>.
     */
    public Integer getPerfilId() {
        return (Integer) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Integer, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UsuariosPerfisRecord
     */
    public UsuariosPerfisRecord() {
        super(UsuariosPerfis.USUARIOS_PERFIS);
    }

    /**
     * Create a detached, initialised UsuariosPerfisRecord
     */
    public UsuariosPerfisRecord(Integer usuarioId, Integer perfilId) {
        super(UsuariosPerfis.USUARIOS_PERFIS);

        setUsuarioId(usuarioId);
        setPerfilId(perfilId);
        resetChangedOnNotNull();
    }
}
