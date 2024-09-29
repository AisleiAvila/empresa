package com.dasad.empresa.repository.query;

import com.dasad.empresa.jooq.tables.Perfis;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.jooq.tables.UsuariosPerfis;
import com.dasad.empresa.models.PerfilModel;
import com.dasad.empresa.models.UsuarioModel;
import jakarta.annotation.Nonnull;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UsuarioQueryBuilder {
    private final SelectOnConditionStep<Record7<Integer, String, String, String, LocalDate, Integer, String>> query;
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
                        Perfis.PERFIS.NOME.as("perfil_nome")
                )
                .from(Usuario.USUARIO)
                .leftJoin(UsuariosPerfis.USUARIOS_PERFIS)
                .on(Usuario.USUARIO.ID.eq(UsuariosPerfis.USUARIOS_PERFIS.USUARIO_ID))
                .leftJoin(Perfis.PERFIS)
                .on(UsuariosPerfis.USUARIOS_PERFIS.PERFIL_ID.eq(Perfis.PERFIS.ID));
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
                Record7<Integer, String, String, String, LocalDate, Integer, String> record = records.get(0);
                UsuarioModel usuario = new UsuarioModel();
                usuario.setId(record.get(Usuario.USUARIO.ID));
                usuario.setNome(record.get(Usuario.USUARIO.NOME));
                usuario.setEmail(record.get(Usuario.USUARIO.EMAIL));
                usuario.setSenha(record.get(Usuario.USUARIO.SENHA));
                usuario.setDataNascimento(record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                usuario.setPerfis(records.stream()
                        .filter(r -> r.get("perfil_id") != null)
                        .map(r -> new PerfilModel(r.get("perfil_id", Integer.class), r.get("perfil_nome", String.class)))
                        .collect(Collectors.toSet()));
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