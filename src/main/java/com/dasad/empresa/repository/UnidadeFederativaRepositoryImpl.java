package com.dasad.empresa.repository;

import com.dasad.empresa.model.UnidadeFederativaModel;
import com.dasad.empresa.model.UnidadeFederativaRequest;
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

    public Optional<List<UnidadeFederativaModel>> find(UnidadeFederativaRequest unidadeFederativaRequest) {

        UnidadeFederativaQueryBuilder queryBuilder = (new UnidadeFederativaQueryBuilder(this.dsl))
                .withId(Optional.ofNullable(unidadeFederativaRequest.getId()))
                .withNome(unidadeFederativaRequest.getNome())
                .withSigla(Optional.ofNullable(unidadeFederativaRequest.getSigla()));
        List<UnidadeFederativaModel> result = queryBuilder.build().join();
        return Optional.ofNullable(result.isEmpty() ? null : result);
    }

}
