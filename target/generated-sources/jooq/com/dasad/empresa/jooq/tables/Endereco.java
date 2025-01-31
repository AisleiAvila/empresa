/*
 * This file is generated by jOOQ.
 */
package com.dasad.empresa.jooq.tables;


import com.dasad.empresa.jooq.Keys;
import com.dasad.empresa.jooq.Public;
import com.dasad.empresa.jooq.tables.UnidadeFederativa.UnidadeFederativaPath;
import com.dasad.empresa.jooq.tables.Usuario.UsuarioPath;
import com.dasad.empresa.jooq.tables.records.EnderecoRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
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
public class Endereco extends TableImpl<EnderecoRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.endereco</code>
     */
    public static final Endereco ENDERECO = new Endereco();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EnderecoRecord> getRecordType() {
        return EnderecoRecord.class;
    }

    /**
     * The column <code>public.endereco.id</code>.
     */
    public final TableField<EnderecoRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.endereco.bairro</code>.
     */
    public final TableField<EnderecoRecord, String> BAIRRO = createField(DSL.name("bairro"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.endereco.cep</code>.
     */
    public final TableField<EnderecoRecord, String> CEP = createField(DSL.name("cep"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.endereco.cidade</code>.
     */
    public final TableField<EnderecoRecord, String> CIDADE = createField(DSL.name("cidade"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.endereco.logradouro</code>.
     */
    public final TableField<EnderecoRecord, String> LOGRADOURO = createField(DSL.name("logradouro"), SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.endereco.usuario_id</code>.
     */
    public final TableField<EnderecoRecord, Integer> USUARIO_ID = createField(DSL.name("usuario_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.endereco.unidade_federativa_id</code>.
     */
    public final TableField<EnderecoRecord, Integer> UNIDADE_FEDERATIVA_ID = createField(DSL.name("unidade_federativa_id"), SQLDataType.INTEGER.identity(true), this, "");

    /**
     * The column <code>public.endereco.numero</code>.
     */
    public final TableField<EnderecoRecord, Integer> NUMERO = createField(DSL.name("numero"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.endereco.complemento</code>.
     */
    public final TableField<EnderecoRecord, String> COMPLEMENTO = createField(DSL.name("complemento"), SQLDataType.VARCHAR(100), this, "");

    private Endereco(Name alias, Table<EnderecoRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Endereco(Name alias, Table<EnderecoRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.endereco</code> table reference
     */
    public Endereco(String alias) {
        this(DSL.name(alias), ENDERECO);
    }

    /**
     * Create an aliased <code>public.endereco</code> table reference
     */
    public Endereco(Name alias) {
        this(alias, ENDERECO);
    }

    /**
     * Create a <code>public.endereco</code> table reference
     */
    public Endereco() {
        this(DSL.name("endereco"), null);
    }

    public <O extends Record> Endereco(Table<O> path, ForeignKey<O, EnderecoRecord> childPath, InverseForeignKey<O, EnderecoRecord> parentPath) {
        super(path, childPath, parentPath, ENDERECO);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class EnderecoPath extends Endereco implements Path<EnderecoRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> EnderecoPath(Table<O> path, ForeignKey<O, EnderecoRecord> childPath, InverseForeignKey<O, EnderecoRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private EnderecoPath(Name alias, Table<EnderecoRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public EnderecoPath as(String alias) {
            return new EnderecoPath(DSL.name(alias), this);
        }

        @Override
        public EnderecoPath as(Name alias) {
            return new EnderecoPath(alias, this);
        }

        @Override
        public EnderecoPath as(Table<?> alias) {
            return new EnderecoPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<EnderecoRecord, Integer> getIdentity() {
        return (Identity<EnderecoRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<EnderecoRecord> getPrimaryKey() {
        return Keys.ENDERECO_PKEY;
    }

    @Override
    public List<ForeignKey<EnderecoRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ENDERECO__FKEKDPB8K6GMP3LLLLA9D1QGMXK, Keys.ENDERECO__FKSBFB2MDC0HMIXWPUD2D8O8QOY);
    }

    private transient UsuarioPath _usuario;

    /**
     * Get the implicit join path to the <code>public.usuario</code> table.
     */
    public UsuarioPath usuario() {
        if (_usuario == null)
            _usuario = new UsuarioPath(this, Keys.ENDERECO__FKEKDPB8K6GMP3LLLLA9D1QGMXK, null);

        return _usuario;
    }

    private transient UnidadeFederativaPath _unidadeFederativa;

    /**
     * Get the implicit join path to the <code>public.unidade_federativa</code>
     * table.
     */
    public UnidadeFederativaPath unidadeFederativa() {
        if (_unidadeFederativa == null)
            _unidadeFederativa = new UnidadeFederativaPath(this, Keys.ENDERECO__FKSBFB2MDC0HMIXWPUD2D8O8QOY, null);

        return _unidadeFederativa;
    }

    @Override
    public Endereco as(String alias) {
        return new Endereco(DSL.name(alias), this);
    }

    @Override
    public Endereco as(Name alias) {
        return new Endereco(alias, this);
    }

    @Override
    public Endereco as(Table<?> alias) {
        return new Endereco(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Endereco rename(String name) {
        return new Endereco(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Endereco rename(Name name) {
        return new Endereco(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Endereco rename(Table<?> name) {
        return new Endereco(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Endereco where(Condition condition) {
        return new Endereco(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Endereco where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Endereco where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Endereco where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Endereco where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Endereco where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Endereco where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Endereco where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Endereco whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Endereco whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
