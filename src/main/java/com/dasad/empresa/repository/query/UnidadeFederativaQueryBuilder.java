package com.dasad.empresa.repository.query;

import com.dasad.empresa.jooq.tables.UnidadeFederativa;
import com.dasad.empresa.model.UnidadeFederativaModel;
import jakarta.annotation.Nonnull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UnidadeFederativaQueryBuilder {
    private final SelectJoinStep<Record> query;

    public UnidadeFederativaQueryBuilder(DSLContext db) {
        this.query = db.select(new SelectFieldOrAsterisk[0]).from(UnidadeFederativa.UNIDADE_FEDERATIVA);
    }

    public UnidadeFederativaQueryBuilder withNome(@Nonnull String nome) {
        if (nome != null &&  !nome.isEmpty()) {
            this.query.where(DSL.lower(UnidadeFederativa.UNIDADE_FEDERATIVA.NOME).like("%" + nome.toLowerCase() + "%"));
        }
        return this;
    }

    public UnidadeFederativaQueryBuilder withSigla(@Nonnull Optional<String> optionalSigla) {
        optionalSigla.ifPresent((sigla) -> this.query.where(DSL.lower(UnidadeFederativa.UNIDADE_FEDERATIVA.SIGLA).like("%" + sigla.toLowerCase() + "%")));
        return this;
    }

    public UnidadeFederativaQueryBuilder withId(@Nonnull Optional<Integer> optionalId) {
        optionalId.ifPresent((sigla) -> this.query.where(UnidadeFederativa.UNIDADE_FEDERATIVA.ID.eq(sigla)));
        return this;
    }

    public CompletableFuture<List<UnidadeFederativaModel>> build() {
        return CompletableFuture.supplyAsync(() -> this.query.fetchInto(UnidadeFederativaModel.class));
    }
}
