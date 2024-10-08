/*
 * This file is generated by jOOQ.
 */
package com.dasad.empresa.jooq.tables.records;


import com.dasad.empresa.jooq.tables.PerfilModel;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PerfilModelRecord extends UpdatableRecordImpl<PerfilModelRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.perfil_model.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.perfil_model.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.perfil_model.nome</code>.
     */
    public void setNome(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.perfil_model.nome</code>.
     */
    public String getNome() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PerfilModelRecord
     */
    public PerfilModelRecord() {
        super(PerfilModel.PERFIL_MODEL);
    }

    /**
     * Create a detached, initialised PerfilModelRecord
     */
    public PerfilModelRecord(Integer id, String nome) {
        super(PerfilModel.PERFIL_MODEL);

        setId(id);
        setNome(nome);
        resetChangedOnNotNull();
    }
}
