/*
 * This file is generated by jOOQ.
 */
package com.dasad.empresa.jooq.tables.records;


import com.dasad.empresa.jooq.tables.ItensPedidos;

import java.math.BigDecimal;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ItensPedidosRecord extends UpdatableRecordImpl<ItensPedidosRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.itens_pedidos.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.itens_pedidos.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.itens_pedidos.id_pedido</code>.
     */
    public void setIdPedido(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.itens_pedidos.id_pedido</code>.
     */
    public Integer getIdPedido() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.itens_pedidos.nome_produto</code>.
     */
    public void setNomeProduto(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.itens_pedidos.nome_produto</code>.
     */
    public String getNomeProduto() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.itens_pedidos.quantidade</code>.
     */
    public void setQuantidade(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.itens_pedidos.quantidade</code>.
     */
    public Integer getQuantidade() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.itens_pedidos.valor_total</code>.
     */
    public void setValorTotal(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.itens_pedidos.valor_total</code>.
     */
    public BigDecimal getValorTotal() {
        return (BigDecimal) get(4);
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
     * Create a detached ItensPedidosRecord
     */
    public ItensPedidosRecord() {
        super(ItensPedidos.ITENS_PEDIDOS);
    }

    /**
     * Create a detached, initialised ItensPedidosRecord
     */
    public ItensPedidosRecord(Integer id, Integer idPedido, String nomeProduto, Integer quantidade, BigDecimal valorTotal) {
        super(ItensPedidos.ITENS_PEDIDOS);

        setId(id);
        setIdPedido(idPedido);
        setNomeProduto(nomeProduto);
        setQuantidade(quantidade);
        setValorTotal(valorTotal);
        resetChangedOnNotNull();
    }
}
