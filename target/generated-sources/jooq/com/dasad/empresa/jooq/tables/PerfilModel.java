/*
 * This file is generated by jOOQ.
 */
package com.dasad.empresa.jooq.tables;


import com.dasad.empresa.jooq.Keys;
import com.dasad.empresa.jooq.Public;
import com.dasad.empresa.jooq.tables.records.PerfilModelRecord;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PerfilModel extends TableImpl<PerfilModelRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.perfil_model</code>
     */
    public static final PerfilModel PERFIL_MODEL = new PerfilModel();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PerfilModelRecord> getRecordType() {
        return PerfilModelRecord.class;
    }

    /**
     * The column <code>public.perfil_model.id</code>.
     */
    public final TableField<PerfilModelRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.perfil_model.nome</code>.
     */
    public final TableField<PerfilModelRecord, String> NOME = createField(DSL.name("nome"), SQLDataType.VARCHAR(255), this, "");

    private PerfilModel(Name alias, Table<PerfilModelRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private PerfilModel(Name alias, Table<PerfilModelRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.perfil_model</code> table reference
     */
    public PerfilModel(String alias) {
        this(DSL.name(alias), PERFIL_MODEL);
    }

    /**
     * Create an aliased <code>public.perfil_model</code> table reference
     */
    public PerfilModel(Name alias) {
        this(alias, PERFIL_MODEL);
    }

    /**
     * Create a <code>public.perfil_model</code> table reference
     */
    public PerfilModel() {
        this(DSL.name("perfil_model"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<PerfilModelRecord, Integer> getIdentity() {
        return (Identity<PerfilModelRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<PerfilModelRecord> getPrimaryKey() {
        return Keys.PERFIL_MODEL_PKEY;
    }

    @Override
    public PerfilModel as(String alias) {
        return new PerfilModel(DSL.name(alias), this);
    }

    @Override
    public PerfilModel as(Name alias) {
        return new PerfilModel(alias, this);
    }

    @Override
    public PerfilModel as(Table<?> alias) {
        return new PerfilModel(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PerfilModel rename(String name) {
        return new PerfilModel(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PerfilModel rename(Name name) {
        return new PerfilModel(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PerfilModel rename(Table<?> name) {
        return new PerfilModel(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PerfilModel where(Condition condition) {
        return new PerfilModel(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PerfilModel where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PerfilModel where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PerfilModel where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PerfilModel where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PerfilModel where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PerfilModel where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PerfilModel where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PerfilModel whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PerfilModel whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
