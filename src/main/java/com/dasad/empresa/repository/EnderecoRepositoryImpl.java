package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.Endereco;
import com.dasad.empresa.models.EnderecoModel;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EnderecoRepositoryImpl implements EnderecoRepository {
    private final DSLContext dsl;

    @Autowired
    public EnderecoRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<EnderecoModel> findAll() {
        return this.dsl.selectFrom(Endereco.ENDERECO).fetchInto(EnderecoModel.class);
    }

    public Optional<EnderecoModel> findById(Integer id) {
        EnderecoModel endereco = (EnderecoModel)this.dsl.selectFrom(Endereco.ENDERECO).where(Endereco.ENDERECO.ID.eq(id)).fetchOneInto(EnderecoModel.class);
        return Optional.ofNullable(endereco);
    }

    public EnderecoModel save(EnderecoModel endereco) {
        this.dsl.insertInto(Endereco.ENDERECO).set(Endereco.ENDERECO.ID, endereco.getId()).set(Endereco.ENDERECO.LOGRADOURO, endereco.getLogradouro()).set(Endereco.ENDERECO.BAIRRO, endereco.getBairro()).set(Endereco.ENDERECO.CIDADE, endereco.getCidade()).set(Endereco.ENDERECO.ESTADO, endereco.getEstado()).set(Endereco.ENDERECO.CEP, endereco.getCep()).execute();
        return endereco;
    }

    public void deleteById(Integer id) {
        this.dsl.deleteFrom(Endereco.ENDERECO).where(Endereco.ENDERECO.ID.eq(id)).execute();
    }
}
