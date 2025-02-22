package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.Cidade;
import com.dasad.empresa.model.CidadeModel;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CidadeRepositoryImpl implements CidadeRepository {
    private final DSLContext dsl;

    @Autowired
    public CidadeRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<CidadeModel> findAll(String nome, Integer estadoId) {
        var query = buildBaseQuery();

        Optional.ofNullable(nome)
                .filter(n -> !n.isEmpty())
                .ifPresent(n -> query.where(Cidade.CIDADE.NOME.likeIgnoreCase("%" + n + "%")));

        Optional.ofNullable(estadoId)
                .ifPresent(id -> query.where(Cidade.CIDADE.ESTADO_ID.eq(id)));

        query.orderBy(Cidade.CIDADE.NOME.asc());

        return query.fetch(CidadeRepositoryImpl::getCidadeModel);
    }

    public Optional<CidadeModel> findById(Integer id) {
        return Optional.ofNullable(
                buildBaseQuery()
                        .where(Cidade.CIDADE.ID.eq(id))
                        .fetchOne(CidadeRepositoryImpl::getCidadeModel)
        );
    }

    private SelectJoinStep<Record2<Integer, String>> buildBaseQuery() {
        return this.dsl.select(
                        Cidade.CIDADE.ID,
                        Cidade.CIDADE.NOME
                )
                .from(Cidade.CIDADE);
    }

    private static CidadeModel getCidadeModel(Record2<Integer, String> record) {
        if (record == null) {
            return null;
        }

        var cidade = new CidadeModel();
        cidade.setId(record.get(Cidade.CIDADE.ID));
        cidade.setNome(record.get(Cidade.CIDADE.NOME));
        return cidade;
    }
}