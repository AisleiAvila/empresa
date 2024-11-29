package com.dasad.empresa.repository.query;

import com.dasad.empresa.jooq.tables.Organizacao;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.model.OrganizacaoModel;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record11;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrganizacaoQueryBuilder {
    private final @NotNull SelectJoinStep<Record11<Integer, String, String, String, String, String, String, String, String, String, LocalDate>> query;
    private final static Integer DEFAULT_LIMIT = 10;
    private final DSLContext dslContext;

    public OrganizacaoQueryBuilder(DSLContext db) {
        this.dslContext = db;
        this.query = db.select(
                        Organizacao.ORGANIZACAO.ID,
                        Organizacao.ORGANIZACAO.NOME,
                        Organizacao.ORGANIZACAO.NIF,
                        Organizacao.ORGANIZACAO.EMAIL,
                        Organizacao.ORGANIZACAO.WEBSITE,
                        Organizacao.ORGANIZACAO.SETOR_ATIVIDADE,
                        Organizacao.ORGANIZACAO.MISSAO,
                        Organizacao.ORGANIZACAO.REPRESENTANTE_LEGAL,
                        Organizacao.ORGANIZACAO.CARGO,
                        Organizacao.ORGANIZACAO.NUMERO_REGISTO_COMERCIAL,
                        Organizacao.ORGANIZACAO.DATA_REGISTO
                )
                .from(Organizacao.ORGANIZACAO);
    }

    public OrganizacaoQueryBuilder withId(@Nonnull Integer id) {
        if(id != null) {
            this.query.where(Organizacao.ORGANIZACAO.ID.eq(id));
        }
        return this;
    }

    public OrganizacaoQueryBuilder withNome(@Nonnull String nome) {
        if (nome != null) {
            this.query.where(DSL.lower(Organizacao.ORGANIZACAO.NOME).like("%" + nome.toLowerCase() + "%"));
        }
        return this;
    }

    public OrganizacaoQueryBuilder withNif(@Nonnull String nif) {
        if (nif != null) {
            this.query.where(DSL.lower(Organizacao.ORGANIZACAO.NIF).like("%" + nif.toLowerCase() + "%"));
        }
        return this;
    }

    public OrganizacaoQueryBuilder withEmail(@Nonnull String email) {
        if (email != null) {
            this.query.where(DSL.lower(Organizacao.ORGANIZACAO.EMAIL).like("%" + email.toLowerCase() + "%"));
        }
        return this;
    }

    public OrganizacaoQueryBuilder withLimit(@Nonnull Integer limit) {
        this.query.limit(limit != null && limit > 0 ? limit : DEFAULT_LIMIT);
        return this;
    }

    public OrganizacaoQueryBuilder withOffset(@Nonnull Integer offset) {
        this.query.offset(offset != null  ? offset : 0);
        return this;
    }

    public CompletableFuture<List<OrganizacaoModel>> build() {
        return CompletableFuture.supplyAsync(() -> {
            return this.query.fetch().stream().collect(Collectors.groupingBy(
                    record -> record.get(Usuario.USUARIO.ID),
                    Collectors.mapping(record -> record, Collectors.toList())
            )).values().stream().map(records -> {
                Record11<Integer, String, String, String, String, String, String, String, String, String, LocalDate> record = records.getFirst();
                OrganizacaoModel organizacao = new OrganizacaoModel();
                organizacao.setId(record.get(Organizacao.ORGANIZACAO.ID));
                organizacao.setNome(record.get(Organizacao.ORGANIZACAO.NOME));
                organizacao.setNif(record.get(Organizacao.ORGANIZACAO.NIF));
                organizacao.setEmail(record.get(Organizacao.ORGANIZACAO.EMAIL));
                organizacao.setWebsite(record.get(Organizacao.ORGANIZACAO.WEBSITE));
                organizacao.setSetorAtividade(record.get(Organizacao.ORGANIZACAO.SETOR_ATIVIDADE));
                organizacao.setMissao(record.get(Organizacao.ORGANIZACAO.MISSAO));
                organizacao.setRepresentanteLegal(record.get(Organizacao.ORGANIZACAO.REPRESENTANTE_LEGAL));
                organizacao.setCargo(record.get(Organizacao.ORGANIZACAO.CARGO));
                organizacao.setNumeroRegistoComercial(record.get(Organizacao.ORGANIZACAO.NUMERO_REGISTO_COMERCIAL));
                LocalDate dataRegisto = record.get(Organizacao.ORGANIZACAO.DATA_REGISTO);
                organizacao.setDataRegisto(dataRegisto != null ? JsonNullable.of(dataRegisto) : JsonNullable.undefined());
                return organizacao;
            }).collect(Collectors.toList());
        });
    }

    public CompletableFuture<Integer> calculateTotalPages(Integer limit) {
        int effectiveLimit = (limit != null && limit > 0) ? limit : DEFAULT_LIMIT;
        return countTotalRecords().thenApply(totalRecords -> (int) Math.ceil((double) totalRecords / effectiveLimit));
    }

    public CompletableFuture<Integer> countTotalRecords() {
        return CompletableFuture.supplyAsync(() -> {
            return this.dslContext
                    .selectCount()
                    .from(Organizacao.ORGANIZACAO)
                    .fetchOne(0, int.class);
        });
    }
}