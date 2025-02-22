package com.dasad.empresa.repository.query;

import com.dasad.empresa.jooq.tables.Cidade;
import com.dasad.empresa.jooq.tables.Estado;
import com.dasad.empresa.jooq.tables.Pais;
import com.dasad.empresa.jooq.tables.Perfil;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.jooq.tables.UsuarioPerfil;
import com.dasad.empresa.model.CidadeModel;
import com.dasad.empresa.model.EnderecoModel;
import com.dasad.empresa.model.EstadoModel;
import com.dasad.empresa.model.PaisModel;
import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.model.UsuarioModel;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record20;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.dasad.empresa.jooq.tables.Endereco.ENDERECO;

public class UsuarioQueryBuilder {
    private final @NotNull SelectOnConditionStep<Record20<Integer, String, String, String, LocalDate, Integer, String, Integer, String, String, String, String, Integer, String, String, Integer, String, Integer, String, Integer>> query;
    private final static Integer DEFAULT_LIMIT = 10;
    private final DSLContext dslContext;

    public UsuarioQueryBuilder(DSLContext db) {
        this.dslContext = db;
        this.query = db.select(
                        Usuario.USUARIO.ID.as("usuario_id"),
                        Usuario.USUARIO.NOME,
                        Usuario.USUARIO.EMAIL,
                        Usuario.USUARIO.SENHA,
                        Usuario.USUARIO.DATA_NASCIMENTO,
                        Perfil.PERFIL.ID.as("perfil_id"),
                        Perfil.PERFIL.NOME.as("perfil_nome"),
                        ENDERECO.ID.as("endereco_id"),
                        ENDERECO.LOGRADOURO,
                        ENDERECO.NUMERO,
                        ENDERECO.COMPLEMENTO,
                        ENDERECO.BAIRRO,
                        ENDERECO.CIDADE_ID,
                        ENDERECO.CEP,
                        Cidade.CIDADE.NOME.as("cidade_nome"),
                        Cidade.CIDADE.ESTADO_ID.as("cidade_estado_id"),
                        Estado.ESTADO.NOME.as("estado_nome"),
                        Estado.ESTADO.ID.as("estado_id"),
                        Pais.PAIS.NOME.as("pais_nome"),
                        Pais.PAIS.ID.as("pais_id")
                )
                .from(Usuario.USUARIO)
                .leftJoin(UsuarioPerfil.USUARIO_PERFIL)
                .on(Usuario.USUARIO.ID.eq(UsuarioPerfil.USUARIO_PERFIL.USUARIO_ID))
                .leftJoin(Perfil.PERFIL)
                .on(UsuarioPerfil.USUARIO_PERFIL.PERFIL_ID.eq(Perfil.PERFIL.ID))
                .leftJoin(ENDERECO).on(Usuario.USUARIO.ID.eq(ENDERECO.USUARIO_ID))
                .leftJoin(Cidade.CIDADE).on(ENDERECO.CIDADE_ID.eq(Cidade.CIDADE.ID))
                .leftJoin(Estado.ESTADO).on(Cidade.CIDADE.ESTADO_ID.eq(Estado.ESTADO.ID))
                .leftJoin(Pais.PAIS).on(Estado.ESTADO.PAIS_ID.eq(Pais.PAIS.ID));
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

    public UsuarioQueryBuilder withPerfil(@Nonnull List<Integer> perfis) {
        if(perfis != null && !perfis.isEmpty()) {
            this.query.where(Perfil.PERFIL.ID.in(perfis));
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
                    record -> record.get("usuario_id", Integer.class),
                    Collectors.mapping(record -> record, Collectors.toList())
            )).values().stream().map(records -> {
                Record20<Integer, String, String, String, LocalDate, Integer, String, Integer, String, String, String, String, Integer, String, String, Integer, String, Integer, String, Integer> record = records.get(0);
                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(record.get("usuario_id", Integer.class));
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
                        .filter(r -> r.get(ENDERECO.BAIRRO) != null)
                        .map(r -> {
                            var endereco = new EnderecoModel();
                            endereco.setId(r.get("endereco_id", Integer.class));
                            endereco.setLogradouro(r.get(ENDERECO.LOGRADOURO));
                            endereco.setNumero(r.get(ENDERECO.NUMERO));
                            endereco.setComplemento(r.get(ENDERECO.COMPLEMENTO));
                            endereco.setBairro(r.get(ENDERECO.BAIRRO));
                            var pais = new PaisModel();
                            pais.setId(r.get("pais_id", Integer.class));
                            pais.setNome(r.get("pais_nome", String.class));
                            var estado = new EstadoModel();
                            estado.setId(r.get("estado_id", Integer.class));
                            estado.setNome(r.get("estado_nome", String.class));
                            estado.setPaisId(pais);
                            var cidade = new CidadeModel();
                            cidade.setId(r.get(ENDERECO.CIDADE_ID));
                            cidade.setNome(r.get("cidade_nome", String.class));
                            cidade.setEstadoId(estado);
                            endereco.setCidadeId(cidade);
                            endereco.setCep(r.get(ENDERECO.CEP));
                            return endereco;
                        })
                        .collect(Collectors.toList()));
                return usuario;
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
                    .from(Usuario.USUARIO)
                    .fetchOne(0, int.class);
        });
    }
}