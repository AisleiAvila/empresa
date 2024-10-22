package com.dasad.empresa.repository.query;

import com.dasad.empresa.jooq.tables.Endereco;
import com.dasad.empresa.jooq.tables.Perfis;
import com.dasad.empresa.jooq.tables.UnidadeFederativa;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.jooq.tables.UsuariosPerfis;
import com.dasad.empresa.model.EnderecoModel;
import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.model.UnidadeFederativaModel;
import com.dasad.empresa.model.UsuarioModel;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record17;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UsuarioQueryBuilder {
    private final @NotNull SelectOnConditionStep<Record17<Integer, String, String, String, LocalDate, Integer, String, Integer, String, Integer, String, String, String, String, Integer, String, String>> query;
    private final static Integer DEFAULT_LIMIT = 10;
    private final DSLContext dslContext;

    public UsuarioQueryBuilder(DSLContext db) {
        this.dslContext = db;
        this.query = db.select(
                        Usuario.USUARIO.ID,
                        Usuario.USUARIO.NOME,
                        Usuario.USUARIO.EMAIL,
                        Usuario.USUARIO.SENHA,
                        Usuario.USUARIO.DATA_NASCIMENTO,
                        Perfis.PERFIS.ID.as("perfil_id"),
                        Perfis.PERFIS.NOME.as("perfil_nome"),
                        Endereco.ENDERECO.ID.as("endereco_id"),
                        Endereco.ENDERECO.LOGRADOURO,
                        Endereco.ENDERECO.NUMERO,
                        Endereco.ENDERECO.COMPLEMENTO,
                        Endereco.ENDERECO.BAIRRO,
                        Endereco.ENDERECO.CIDADE,
                        Endereco.ENDERECO.CEP,
                        UnidadeFederativa.UNIDADE_FEDERATIVA.ID.as("unidade_federatival_id"),
                        UnidadeFederativa.UNIDADE_FEDERATIVA.SIGLA,
                        UnidadeFederativa.UNIDADE_FEDERATIVA.NOME
                )
                .from(Usuario.USUARIO)
                .leftJoin(UsuariosPerfis.USUARIOS_PERFIS)
                .on(Usuario.USUARIO.ID.eq(UsuariosPerfis.USUARIOS_PERFIS.USUARIO_ID))
                .leftJoin(Perfis.PERFIS)
                .on(UsuariosPerfis.USUARIOS_PERFIS.PERFIL_ID.eq(Perfis.PERFIS.ID))
                .leftJoin(Endereco.ENDERECO).on(Usuario.USUARIO.ID.eq(Endereco.ENDERECO.USUARIO_ID))
                .leftJoin(UnidadeFederativa.UNIDADE_FEDERATIVA).on(Endereco.ENDERECO.UNIDADE_FEDERATIVA_ID.eq(UnidadeFederativa.UNIDADE_FEDERATIVA.ID));
    }

    public UsuarioQueryBuilder withNome(@Nonnull String nome) {
        if (nome != null) {
            this.query.where(DSL.lower(Usuario.USUARIO.NOME).like("%" + nome.toLowerCase() + "%"));
        }
        return this;
    }

    public UsuarioQueryBuilder withEmail(@Nonnull String email) {
        if (email != null) {
            this.query.where(DSL.lower(Usuario.USUARIO.EMAIL).like("%" + email.toLowerCase() + "%"));
        }
        return this;
    }

    public UsuarioQueryBuilder withDataNascimento(@Nonnull LocalDate dataNascimento) {
        if (dataNascimento != null) {
            this.query.where(Usuario.USUARIO.DATA_NASCIMENTO.eq(dataNascimento));
        }
        return this;
    }

    public UsuarioQueryBuilder withId(@Nonnull Integer id) {
        if(id != null) {
            this.query.where(Usuario.USUARIO.ID.eq(id));
        }
        return this;
    }

    public UsuarioQueryBuilder withLimit(@Nonnull Integer limit) {
        this.query.limit(limit != null && limit > 0 ? limit : DEFAULT_LIMIT);
        return this;
    }

    public UsuarioQueryBuilder withOffset(@Nonnull Integer offset) {
        this.query.offset(offset != null  ? offset : 0);
        return this;
    }

    public CompletableFuture<List<UsuarioModel>> build() {
        return CompletableFuture.supplyAsync(() -> {
            return this.query.fetch().stream().collect(Collectors.groupingBy(
                    record -> record.get(Usuario.USUARIO.ID),
                    Collectors.mapping(record -> record, Collectors.toList())
            )).values().stream().map(records -> {
                Record17<Integer, String, String, String, LocalDate, Integer, String, Integer, String, Integer, String, String, String, String, Integer, String, String> record = records.get(0);
                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(record.get(Usuario.USUARIO.ID));
                usuario.setNome(record.get(Usuario.USUARIO.NOME));
                usuario.setEmail(record.get(Usuario.USUARIO.EMAIL));
                usuario.setSenha(record.get(Usuario.USUARIO.SENHA));
                usuario.setDataNascimento(record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                usuario.setPerfis(records.stream()
                        .filter(r -> r.get("perfil_id") != null)
                        .map(r -> {
                            PerfilModel perfil = new PerfilModel();
                            perfil.setId(r.get("perfil_id", Integer.class));
                            perfil.setNome(r.get("perfil_nome", String.class));
                            return perfil;
                        })
                        .collect(Collectors.toList()));
                usuario.setEnderecos(records.stream()
                        .filter(r -> r.get(Endereco.ENDERECO.BAIRRO) != null)
                        .map(r -> {
                            var endereco = new EnderecoModel();
                            endereco.setId(r.get(Endereco.ENDERECO.ID));
                            endereco.setLogradouro(r.get(Endereco.ENDERECO.LOGRADOURO));
                            endereco.setNumero(r.get(Endereco.ENDERECO.NUMERO));
                            endereco.setComplemento(r.get(Endereco.ENDERECO.COMPLEMENTO));
                            endereco.setBairro(r.get(Endereco.ENDERECO.BAIRRO));
                            endereco.setCidade(r.get(Endereco.ENDERECO.CIDADE));
                            endereco.setCep(r.get(Endereco.ENDERECO.CEP));
                            if (r.get(UnidadeFederativa.UNIDADE_FEDERATIVA.ID) != null) {
                                var unidadeFederativaModel = new UnidadeFederativaModel();
                                unidadeFederativaModel.setId(r.get(UnidadeFederativa.UNIDADE_FEDERATIVA.ID));
                                unidadeFederativaModel.setSigla(r.get(UnidadeFederativa.UNIDADE_FEDERATIVA.SIGLA));
                                unidadeFederativaModel.setNome(r.get(UnidadeFederativa.UNIDADE_FEDERATIVA.NOME));
                                endereco.setUf(unidadeFederativaModel);
                            }

                            return endereco;
                        })
                        .collect(Collectors.toList()));
                return usuario;
            }).collect(Collectors.toList());
        });
    }

    public CompletableFuture<Integer> calculateTotalPages(Integer limit) {
        int effectiveLimit = (limit != null && limit > 0) ? limit : DEFAULT_LIMIT;
        return countTotalRecords().thenApply(totalRecords -> {
            return (int) Math.ceil((double) totalRecords / effectiveLimit);
        });
    }

    public CompletableFuture<Integer> countTotalRecords() {
        return CompletableFuture.supplyAsync(() -> {
            return this.dslContext
                    .selectCount()
                    .from(Usuario.USUARIO)
                    .fetchOne(0, int.class);
        });
    }
}