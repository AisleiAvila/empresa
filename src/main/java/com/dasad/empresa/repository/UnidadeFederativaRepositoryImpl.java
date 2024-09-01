package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.UnidadeFederativa;
import com.dasad.empresa.models.UnidadeFederativaModel;
import com.dasad.empresa.models.request.UnidadeFederativaRequest;
import com.dasad.empresa.repository.query.UnidadeFederativaQueryBuilder;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UnidadeFederativaRepositoryImpl implements UnidadeFederativaRepository {
    private final DSLContext dsl;

    @Autowired
    public UnidadeFederativaRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<List<UnidadeFederativaModel>> findByNome(UnidadeFederativaRequest unidadeFederativaRequest) {
        UnidadeFederativaQueryBuilder queryBuilder = (new UnidadeFederativaQueryBuilder(this.dsl)).withNome(unidadeFederativaRequest.getNome()).withSigla(unidadeFederativaRequest.getSigla());
        List<UnidadeFederativaModel> result = (List)queryBuilder.build().join();
        return Optional.ofNullable(result.isEmpty() ? null : result);
    }

    public UnidadeFederativaModel save(UnidadeFederativaModel unidadeFederativa) {
        this.dsl.insertInto(UnidadeFederativa.UNIDADE_FEDERATIVA).set(UnidadeFederativa.UNIDADE_FEDERATIVA.ID, unidadeFederativa.getId()).set(UnidadeFederativa.UNIDADE_FEDERATIVA.NOME, unidadeFederativa.getNome()).set(UnidadeFederativa.UNIDADE_FEDERATIVA.SIGLA, unidadeFederativa.getSigla()).execute();
        return unidadeFederativa;
    }

    public void deleteById(Integer id) {
        this.dsl.deleteFrom(UnidadeFederativa.UNIDADE_FEDERATIVA).where(UnidadeFederativa.UNIDADE_FEDERATIVA.ID.eq(id)).execute();
    }
}
