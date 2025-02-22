package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.Estado;
import com.dasad.empresa.model.EstadoModel;
import com.dasad.empresa.model.PaisModel;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EstadoRepositoryImpl implements EstadoRepository {
    private final DSLContext dsl;

    @Autowired
    public EstadoRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<EstadoModel> findAll(String nome, Integer paisId) {
        var query = buildBaseQuery();

        Optional.ofNullable(nome)
                .filter(n -> !n.isEmpty())
                .ifPresent(n -> query.where(Estado.ESTADO.NOME.likeIgnoreCase("%" + n + "%")));

        Optional.ofNullable(paisId)
                .ifPresent(id -> query.where(Estado.ESTADO.PAIS_ID.eq(id)));

        query.orderBy(Estado.ESTADO.NOME.asc());

        return query.fetch(EstadoRepositoryImpl::getEstadoModel);
    }

    public Optional<EstadoModel> findById(Integer id) {
        return Optional.ofNullable(
                buildBaseQuery()
                        .where(Estado.ESTADO.ID.eq(id))
                        .fetchOne(EstadoRepositoryImpl::getEstadoModel)
        );
    }

    private SelectJoinStep<Record3<Integer, String, Integer>> buildBaseQuery() {
        return this.dsl.select(
                        Estado.ESTADO.ID,
                        Estado.ESTADO.NOME,
                        Estado.ESTADO.PAIS_ID
                )
                .from(Estado.ESTADO);
    }

    private static EstadoModel getEstadoModel(Record3<Integer, String, Integer> record) {
        if (record == null) {
            return null;
        }

        var estado = new EstadoModel();
        estado.setId(record.get(Estado.ESTADO.ID));
        estado.setNome(record.get(Estado.ESTADO.NOME));
        var pais = new PaisModel();
        pais.setId(record.get(Estado.ESTADO.PAIS_ID));
        estado.setPaisId(pais);
        return estado;
    }
}