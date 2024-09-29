package com.dasad.empresa.repository;

import com.dasad.empresa.exception.EmailAlreadyExistsException;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.models.PerfilModel;
import com.dasad.empresa.models.UsuarioModel;
import com.dasad.empresa.models.request.UsuarioRequest;
import com.dasad.empresa.repository.query.UsuarioQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dasad.empresa.jooq.tables.Perfis.PERFIS;
import static com.dasad.empresa.jooq.tables.UsuariosPerfis.USUARIOS_PERFIS;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private static final Logger log = LogManager.getLogger(UsuarioRepositoryImpl.class);
    private final DSLContext dsl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioRepositoryImpl(DSLContext dsl, PasswordEncoder passwordEncoder) {
        this.dsl = dsl;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<List<UsuarioModel>> find(UsuarioRequest usuarioRequest) {
        UsuarioQueryBuilder queryBuilder = (new UsuarioQueryBuilder(this.dsl))
                .withId(usuarioRequest.getId())
                .withNome(usuarioRequest.getNome())
                .withEmail(usuarioRequest.getEmail())
                .withDataNascimento(usuarioRequest.getDataNascimento())
                .withLimit(usuarioRequest.getLimit())
                .withOffset(usuarioRequest.getOffset());
        List<UsuarioModel> result = queryBuilder.build().join();
        return Optional.ofNullable(result.isEmpty() ? null : result);
    }

    public Optional<Integer> countTotalRecords(UsuarioRequest usuarioRequest) {
        UsuarioQueryBuilder queryBuilder = (new UsuarioQueryBuilder(this.dsl))
                .withId(usuarioRequest.getId())
                .withNome(usuarioRequest.getNome())
                .withEmail(usuarioRequest.getEmail())
                .withDataNascimento(usuarioRequest.getDataNascimento())
                .withLimit(0)
                .withOffset(0);
        var result = queryBuilder.countTotalRecords().join();
        return Optional.ofNullable(result == null ? 0 : result);
    }


    public Optional<UsuarioModel> findById(Integer id) {
        return dsl.select(Usuario.USUARIO.fields())
                .select(USUARIOS_PERFIS.PERFIL_ID)
                .select(PERFIS.NOME)
                .from(Usuario.USUARIO)
                .leftJoin(USUARIOS_PERFIS).on(Usuario.USUARIO.ID.eq(USUARIOS_PERFIS.USUARIO_ID))
                .leftJoin(PERFIS).on(USUARIOS_PERFIS.PERFIL_ID.eq(PERFIS.ID))
                .where(Usuario.USUARIO.ID.eq(id))
                .fetchOptional()
                .map(record -> {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setId(record.get(Usuario.USUARIO.ID));
                    usuario.setNome(record.get(Usuario.USUARIO.NOME));
                    usuario.setEmail(record.get(Usuario.USUARIO.EMAIL));
                    usuario.setSenha(record.get(Usuario.USUARIO.SENHA));
                    usuario.setDataNascimento(record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                    if (record.get(USUARIOS_PERFIS.PERFIL_ID) != null) {
                        usuario.setPerfis(Collections.singleton(new PerfilModel(record.get(USUARIOS_PERFIS.PERFIL_ID), record.get(PERFIS.NOME))));
                    } else {
                        usuario.setPerfis(Collections.emptySet());
                    }
                    return usuario;
                });
    }

    public Optional<UsuarioModel> findByEmail(String email) {
        return dsl.select(Usuario.USUARIO.fields())
                .select(USUARIOS_PERFIS.PERFIL_ID)
                .select(PERFIS.NOME)
                .from(Usuario.USUARIO)
                .leftJoin(USUARIOS_PERFIS).on(Usuario.USUARIO.ID.eq(USUARIOS_PERFIS.USUARIO_ID))
                .leftJoin(PERFIS).on(USUARIOS_PERFIS.PERFIL_ID.eq(PERFIS.ID))
                .where(Usuario.USUARIO.EMAIL.eq(email))
                .fetchOptional()
                .map(record -> {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setId(record.get(Usuario.USUARIO.ID));
                    usuario.setNome(record.get(Usuario.USUARIO.NOME));
                    usuario.setEmail(record.get(Usuario.USUARIO.EMAIL));
                    usuario.setSenha(record.get(Usuario.USUARIO.SENHA));
                    usuario.setDataNascimento(record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                    if (record.get(USUARIOS_PERFIS.PERFIL_ID) != null) {
                        usuario.setPerfis(Collections.singleton(new PerfilModel(record.get(USUARIOS_PERFIS.PERFIL_ID), record.get(PERFIS.NOME))));
                    } else {
                        usuario.setPerfis(Collections.emptySet());
                    }
                    return usuario;
                });
    }

    public UsuarioModel create(UsuarioModel usuario) {
        // Verificar se o email já existe
        boolean emailExists = dsl.fetchExists(
                dsl.selectFrom(Usuario.USUARIO)
                        .where(DSL.lower(Usuario.USUARIO.EMAIL).eq(usuario.getEmail().toLowerCase()))
        );

        if (emailExists) {
            throw new EmailAlreadyExistsException("Email já existe: " + usuario.getEmail());
        }

        return dsl.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            // Criptografar a senha
            String encryptedPassword = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(encryptedPassword);

            // Inserir o usuário
            ctx.insertInto(Usuario.USUARIO)
                    .set(Usuario.USUARIO.NOME, usuario.getNome())
                    .set(Usuario.USUARIO.EMAIL, usuario.getEmail())
                    .set(Usuario.USUARIO.SENHA, usuario.getSenha())
                    .set(Usuario.USUARIO.DATA_NASCIMENTO, usuario.getDataNascimento())
                    .execute();

            // Obter o ID gerado
            Integer userId = ctx.select(Usuario.USUARIO.ID)
                    .from(Usuario.USUARIO)
                    .where(Usuario.USUARIO.EMAIL.eq(usuario.getEmail()))
                    .fetchOneInto(Integer.class);
            usuario.setId(userId);

            // Gravar a lista de perfis do usuário
            for (PerfilModel perfil : usuario.getPerfis()) {
                ctx.insertInto(USUARIOS_PERFIS)
                        .set(USUARIOS_PERFIS.USUARIO_ID, userId)
                        .set(USUARIOS_PERFIS.PERFIL_ID, perfil.getId())
                        .execute();
            }

            // TODO gravar a lista de enderecos do usuario

            return usuario;
        });
    }

    public UsuarioModel update(UsuarioModel usuario) {
        return dsl.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            // Atualizar o usuário
            ctx.update(Usuario.USUARIO)
                    .set(Usuario.USUARIO.NOME, usuario.getNome())
                    .set(Usuario.USUARIO.EMAIL, usuario.getEmail())
                    .set(Usuario.USUARIO.DATA_NASCIMENTO, usuario.getDataNascimento())
                    .where(Usuario.USUARIO.ID.eq(usuario.getId()))
                    .execute();

            // Remover os perfis antigos
            ctx.deleteFrom(USUARIOS_PERFIS)
                    .where(USUARIOS_PERFIS.USUARIO_ID.eq(usuario.getId()))
                    .execute();

            // Gravar a lista de perfis do usuário
            for (PerfilModel perfil : usuario.getPerfis()) {
                ctx.insertInto(USUARIOS_PERFIS)
                        .set(USUARIOS_PERFIS.USUARIO_ID, usuario.getId())
                        .set(USUARIOS_PERFIS.PERFIL_ID, perfil.getId())
                        .execute();
            }

            // TODO gravar a lista de enderecos do usuario

            // Buscar o usuário atualizado
            return ctx.select(Usuario.USUARIO.fields())
                    .select(USUARIOS_PERFIS.PERFIL_ID)
                    .select(PERFIS.NOME)
                    .from(Usuario.USUARIO)
                    .leftJoin(USUARIOS_PERFIS).on(Usuario.USUARIO.ID.eq(USUARIOS_PERFIS.USUARIO_ID))
                    .leftJoin(PERFIS).on(USUARIOS_PERFIS.PERFIL_ID.eq(PERFIS.ID))
                    .where(Usuario.USUARIO.ID.eq(usuario.getId()))
                    .fetchOne(record -> {
                        UsuarioModel updatedUsuario = new UsuarioModel();
                        updatedUsuario.setId(record.get(Usuario.USUARIO.ID));
                        updatedUsuario.setNome(record.get(Usuario.USUARIO.NOME));
                        updatedUsuario.setEmail(record.get(Usuario.USUARIO.EMAIL));
                        updatedUsuario.setSenha(record.get(Usuario.USUARIO.SENHA));
                        updatedUsuario.setDataNascimento(record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                        if (record.get(USUARIOS_PERFIS.PERFIL_ID) != null) {
                            updatedUsuario.setPerfis(Collections.singleton(new PerfilModel(record.get(USUARIOS_PERFIS.PERFIL_ID), record.get(PERFIS.NOME))));
                        } else {
                            updatedUsuario.setPerfis(Collections.emptySet());
                        }
                        return updatedUsuario;
                    });
        });
    }
    public void deleteById(Integer id) {
        this.dsl.deleteFrom(Usuario.USUARIO).where(Usuario.USUARIO.ID.eq(id)).execute();
    }

}
