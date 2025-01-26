package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.Cidade;
import com.dasad.empresa.jooq.tables.Endereco2;
import com.dasad.empresa.jooq.tables.Estado;
import com.dasad.empresa.jooq.tables.Pais;
import com.dasad.empresa.model.CidadeModel;
import com.dasad.empresa.model.Endereco2Model;
import com.dasad.empresa.model.EstadoModel;
import com.dasad.empresa.model.PaisModel;
import org.jooq.DSLContext;
import org.jooq.Record15;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class Endereco2RepositoryImpl implements Endereco2Repository {
    private final DSLContext dsl;

    @Autowired
    public Endereco2RepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<Endereco2Model> findAll() {
        return buildBaseQuery()
                .fetch(Endereco2RepositoryImpl::getEndereco2Model);
    }

    public Optional<Endereco2Model> findById(Integer id) {
        return Optional.ofNullable(
                buildBaseQuery()
                        .where(Endereco2.ENDERECO2.ID.eq(id))
                        .fetchOne(Endereco2RepositoryImpl::getEndereco2Model)
        );
    }

    public Endereco2Model save(Endereco2Model endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereco não pode ser nulo.");
        }
        if (endereco.getCidadeId() == null || endereco.getCep() == null || endereco.getLogradouro() == null) {
            throw new IllegalArgumentException("Campos obrigatórios não podem ser nulos.");
        }

        this.dsl.insertInto(Endereco2.ENDERECO2)
                .set(Endereco2.ENDERECO2.ID, endereco.getId())
                .set(Endereco2.ENDERECO2.CIDADE_ID, endereco.getCidadeId().getId())
                .set(Endereco2.ENDERECO2.CEP, endereco.getCep())
                .set(Endereco2.ENDERECO2.LOGRADOURO, endereco.getLogradouro())
                .set(Endereco2.ENDERECO2.NUMERO, endereco.getNumero())
                .set(Endereco2.ENDERECO2.COMPLEMENTO, endereco.getComplemento())
                .set(Endereco2.ENDERECO2.BAIRRO, endereco.getBairro())
                .set(Endereco2.ENDERECO2.LATITUDE, endereco.getLatitude())
                .set(Endereco2.ENDERECO2.LONGITUDE, endereco.getLongitude())
                .set(Endereco2.ENDERECO2.USUARIO_ID, endereco.getUsuarioId())
                .execute();
        return endereco;
    }

    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }

        this.dsl.deleteFrom(Endereco2.ENDERECO2).where(Endereco2.ENDERECO2.ID.eq(id)).execute();
    }

    private SelectJoinStep<Record15<Integer, String, String, String, String, String, BigDecimal, BigDecimal, Integer, Integer, String, Integer, String, Integer, String>> buildBaseQuery() {
        return this.dsl.select(
                        Endereco2.ENDERECO2.ID,
                        Endereco2.ENDERECO2.LOGRADOURO,
                        Endereco2.ENDERECO2.NUMERO,
                        Endereco2.ENDERECO2.COMPLEMENTO,
                        Endereco2.ENDERECO2.BAIRRO,
                        Endereco2.ENDERECO2.CEP,
                        Endereco2.ENDERECO2.LATITUDE,
                        Endereco2.ENDERECO2.LONGITUDE,
                        Endereco2.ENDERECO2.USUARIO_ID,
                        Cidade.CIDADE.ID.as("cidadeId"),
                        Cidade.CIDADE.NOME.as("cidadeNome"),
                        Estado.ESTADO.ID.as("estadoId"),
                        Estado.ESTADO.NOME.as("estadoNome"),
                        Pais.PAIS.ID.as("paisId"),
                        Pais.PAIS.NOME.as("paisNome")
                )
                .from(Endereco2.ENDERECO2)
                .join(Cidade.CIDADE).on(Endereco2.ENDERECO2.CIDADE_ID.eq(Cidade.CIDADE.ID))
                .join(Estado.ESTADO).on(Cidade.CIDADE.ESTADO_ID.eq(Estado.ESTADO.ID))
                .join(Pais.PAIS).on(Estado.ESTADO.PAIS_ID.eq(Pais.PAIS.ID));
    }

    private static Endereco2Model getEndereco2Model(Record15<Integer, String, String, String, String, String, BigDecimal, BigDecimal, Integer, Integer, String, Integer, String, Integer, String> record) {
        if (record == null) {
            return null;
        }

        Endereco2Model endereco = new Endereco2Model();
        endereco.setId(record.get(Endereco2.ENDERECO2.ID));
        endereco.setLogradouro(record.get(Endereco2.ENDERECO2.LOGRADOURO));
        endereco.setNumero(record.get(Endereco2.ENDERECO2.NUMERO));
        endereco.setComplemento(record.get(Endereco2.ENDERECO2.COMPLEMENTO));
        endereco.setBairro(record.get(Endereco2.ENDERECO2.BAIRRO));
        endereco.setCep(record.get(Endereco2.ENDERECO2.CEP));
        endereco.setLatitude(record.get(Endereco2.ENDERECO2.LATITUDE));
        endereco.setLongitude(record.get(Endereco2.ENDERECO2.LONGITUDE));
        endereco.setUsuarioId(record.get(Endereco2.ENDERECO2.USUARIO_ID));

        endereco.setCidadeId(createCidadeModel(record));
        return endereco;
    }

    private static CidadeModel createCidadeModel(Record15<Integer, String, String, String, String, String, BigDecimal, BigDecimal, Integer, Integer, String, Integer, String, Integer, String> record) {
        CidadeModel cidade = new CidadeModel();
        cidade.setId(record.get(Cidade.CIDADE.ID.as("cidadeId")));
        cidade.setNome(record.get(Cidade.CIDADE.NOME.as("cidadeNome")));
        cidade.setEstadoId(createEstadoModel(record));
        return cidade;
    }

    private static EstadoModel createEstadoModel(Record15<Integer, String, String, String, String, String, BigDecimal, BigDecimal, Integer, Integer, String, Integer, String, Integer, String> record) {
        EstadoModel estado = new EstadoModel();
        estado.setId(record.get(Estado.ESTADO.ID.as("estadoId")));
        estado.setNome(record.get(Estado.ESTADO.NOME.as("estadoNome")));
        estado.setPaisId(createPaisModel(record));
        return estado;
    }

    private static PaisModel createPaisModel(Record15<Integer, String, String, String, String, String, BigDecimal, BigDecimal, Integer, Integer, String, Integer, String, Integer, String> record) {
        PaisModel pais = new PaisModel();
        pais.setId(record.get(Pais.PAIS.ID.as("paisId")));
        pais.setNome(record.get(Pais.PAIS.NOME.as("paisNome")));
        return pais;
    }
}