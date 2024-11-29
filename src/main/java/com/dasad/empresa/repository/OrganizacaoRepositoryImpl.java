package com.dasad.empresa.repository;

import com.dasad.empresa.exception.OrganizacaoAlreadyExistsException;
import com.dasad.empresa.jooq.tables.Organizacao;
import com.dasad.empresa.model.OrganizacaoModel;
import com.dasad.empresa.model.OrganizacaoRequest;
import com.dasad.empresa.repository.query.OrganizacaoQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class OrganizacaoRepositoryImpl implements  OrganizacaoRepository {
    private static final Logger log = LogManager.getLogger(UsuarioRepositoryImpl.class);
    private final DSLContext dsl;

    @Autowired
    public OrganizacaoRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<List<OrganizacaoModel>> find(OrganizacaoRequest organizacaorequest) {
        OrganizacaoQueryBuilder queryBuilder = new OrganizacaoQueryBuilder(this.dsl)
                .withId(organizacaorequest.getId())
                .withNif(organizacaorequest.getNif())
                .withNome(organizacaorequest.getNome())
                .withEmail(organizacaorequest.getEmail())
                .withLimit(organizacaorequest.getLimit())
                .withOffset(organizacaorequest.getOffset());
        List<OrganizacaoModel> result = queryBuilder.build().join();
        return Optional.ofNullable(result.isEmpty() ? null : result);
    }

    @Override
    public Optional<Integer> countTotalRecords(OrganizacaoRequest organizacaorequest) {

        OrganizacaoQueryBuilder queryBuilder = new OrganizacaoQueryBuilder(this.dsl)
                .withId(organizacaorequest.getId())
                .withNome(organizacaorequest.getNome())
                .withNif(organizacaorequest.getNif())
                .withEmail(organizacaorequest.getEmail())
                .withLimit(0)
                .withOffset(0);
        var result = queryBuilder.countTotalRecords().join();
        return Optional.ofNullable(result == null ? 0 : result);
    }

    @Override
    public Optional<OrganizacaoModel> findById(Integer id) {
        return dsl.select(
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
                .from(Organizacao.ORGANIZACAO)
                .where(Organizacao.ORGANIZACAO.ID.eq(id))
                .fetchOptional()
                .map(record -> {
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
                });
    }

    @Override
    public OrganizacaoModel create(OrganizacaoModel organizacao) {
        if (isOrganizacaoNomelExists(organizacao)) {
            throw new OrganizacaoAlreadyExistsException("Organização já existe: " + organizacao.getNome());
        }

        if (isOrganizacaoNifExists(organizacao)) {
            throw new OrganizacaoAlreadyExistsException("NIF já existe: " + organizacao.getNif());
        }

        return dsl.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

             ctx.insertInto(Organizacao.ORGANIZACAO)
                    .set(Organizacao.ORGANIZACAO.NOME, organizacao.getNome())
                    .set(Organizacao.ORGANIZACAO.NIF, organizacao.getNif())
                    .set(Organizacao.ORGANIZACAO.EMAIL, organizacao.getEmail())
                    .set(Organizacao.ORGANIZACAO.WEBSITE, organizacao.getWebsite())
                    .set(Organizacao.ORGANIZACAO.SETOR_ATIVIDADE, organizacao.getSetorAtividade())
                    .set(Organizacao.ORGANIZACAO.MISSAO, organizacao.getMissao())
                    .set(Organizacao.ORGANIZACAO.REPRESENTANTE_LEGAL, organizacao.getRepresentanteLegal())
                    .set(Organizacao.ORGANIZACAO.CARGO, organizacao.getCargo())
                    .set(Organizacao.ORGANIZACAO.NUMERO_REGISTO_COMERCIAL, organizacao.getNumeroRegistoComercial())
                     .set(Organizacao.ORGANIZACAO.DATA_REGISTO, organizacao.getDataRegisto().orElse(null))
                    .execute();

            Integer userId = ctx.select(Organizacao.ORGANIZACAO.ID)
                    .from(Organizacao.ORGANIZACAO)
                    .where(Organizacao.ORGANIZACAO.NIF.eq(organizacao.getNif()))
                    .fetchOneInto(Integer.class);
            organizacao.setId(userId);

            return organizacao;
        });
    }

    @Override
    public OrganizacaoModel update(OrganizacaoModel organizacaoModel) {
        return dsl.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            ctx.update(Organizacao.ORGANIZACAO)
                    .set(Organizacao.ORGANIZACAO.NOME, organizacaoModel.getNome())
                    .set(Organizacao.ORGANIZACAO.NIF, organizacaoModel.getNif())
                    .set(Organizacao.ORGANIZACAO.EMAIL, organizacaoModel.getEmail())
                    .set(Organizacao.ORGANIZACAO.WEBSITE, organizacaoModel.getWebsite())
                    .set(Organizacao.ORGANIZACAO.SETOR_ATIVIDADE, organizacaoModel.getSetorAtividade())
                    .set(Organizacao.ORGANIZACAO.MISSAO, organizacaoModel.getMissao())
                    .set(Organizacao.ORGANIZACAO.REPRESENTANTE_LEGAL, organizacaoModel.getRepresentanteLegal())
                    .set(Organizacao.ORGANIZACAO.CARGO, organizacaoModel.getCargo())
                    .set(Organizacao.ORGANIZACAO.NUMERO_REGISTO_COMERCIAL, organizacaoModel.getNumeroRegistoComercial())
                    .set(Organizacao.ORGANIZACAO.DATA_REGISTO, organizacaoModel.getDataRegisto().orElse(null))
                    .where(Organizacao.ORGANIZACAO.ID.eq(organizacaoModel.getId()))
                    .execute();

            return findById(organizacaoModel.getId()).orElseThrow(() -> new RuntimeException("Organização não encontrada"));
        });
    }

    @Override
    public void deleteById(Integer id) {
        dsl.deleteFrom(Organizacao.ORGANIZACAO).where(Organizacao.ORGANIZACAO.ID.eq(id)).execute();
    }

    private boolean isOrganizacaoNomelExists(OrganizacaoModel organizacaoModel) {
        return dsl.fetchExists(
                dsl.selectFrom(Organizacao.ORGANIZACAO)
                        .where(DSL.lower(Organizacao.ORGANIZACAO.NOME).eq(organizacaoModel.getNome().toLowerCase()))
        );
    }

    private boolean isOrganizacaoNifExists(OrganizacaoModel organizacaoModel) {
        return dsl.fetchExists(
                dsl.selectFrom(Organizacao.ORGANIZACAO)
                        .where(DSL.lower(Organizacao.ORGANIZACAO.NIF).eq(organizacaoModel.getNif().toLowerCase()))
        );
    }
}
