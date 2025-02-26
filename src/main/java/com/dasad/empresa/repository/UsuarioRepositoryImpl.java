package com.dasad.empresa.repository;

import com.dasad.empresa.exception.EmailAlreadyExistsException;
import com.dasad.empresa.jooq.tables.Cidade;
import com.dasad.empresa.jooq.tables.Estado;
import com.dasad.empresa.jooq.tables.Pais;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.jooq.tables.UsuarioPerfil;
import com.dasad.empresa.model.CidadeModel;
import com.dasad.empresa.model.EnderecoModel;
import com.dasad.empresa.model.EstadoModel;
import com.dasad.empresa.model.PaisModel;
import com.dasad.empresa.model.PerfilModel;
import com.dasad.empresa.model.UsuarioModel;
import com.dasad.empresa.model.UsuarioRequest;
import com.dasad.empresa.repository.query.UsuarioQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dasad.empresa.jooq.tables.Endereco.ENDERECO;
import static com.dasad.empresa.jooq.tables.Perfil.PERFIL;
import static com.dasad.empresa.jooq.tables.UsuarioPerfil.USUARIO_PERFIL;

@Repository
//@Transactional
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
        UsuarioQueryBuilder queryBuilder = new UsuarioQueryBuilder(this.dsl)
                .withId(usuarioRequest.getId())
                .withNome(usuarioRequest.getNome())
                .withEmail(usuarioRequest.getEmail())
//                .withDataNascimento(convertStringToLocalDate(usuarioRequest.getDataNascimento()))
                .withDataNascimento(usuarioRequest.getDataNascimento())
                .withPerfil(usuarioRequest.getPerfis())
                .withLimit(usuarioRequest.getLimit())
                .withOffset(usuarioRequest.getOffset());
        List<UsuarioModel> result = queryBuilder.build().join();
        return Optional.ofNullable(result.isEmpty() ? null : result);
    }

    public Optional<Integer> countTotalRecords(UsuarioRequest usuarioRequest) {
        UsuarioQueryBuilder queryBuilder = new UsuarioQueryBuilder(this.dsl)
                .withId(usuarioRequest.getId())
                .withNome(usuarioRequest.getNome())
                .withEmail(usuarioRequest.getEmail())
//                .withDataNascimento(convertStringToLocalDate(usuarioRequest.getDataNascimento()))
                .withDataNascimento(usuarioRequest.getDataNascimento())
                .withPerfil(usuarioRequest.getPerfis())
                .withLimit(0)
                .withOffset(0);
        var result = queryBuilder.countTotalRecords().join();
        return Optional.ofNullable(result == null ? 0 : result);
    }

    public Optional<UsuarioModel> findById(Integer id) {
        return dsl.select(
                        Usuario.USUARIO.ID,
                        Usuario.USUARIO.NOME,
                        Usuario.USUARIO.EMAIL,
                        Usuario.USUARIO.DATA_NASCIMENTO,
                        PERFIL.ID.as("perfil_id"),
                        PERFIL.NOME.as("perfil_nome"),
                        ENDERECO.ID.as("endereco_id"),
                        ENDERECO.LOGRADOURO,
                        ENDERECO.NUMERO,
                        ENDERECO.COMPLEMENTO,
                        ENDERECO.BAIRRO,
                        ENDERECO.CIDADE_ID.as("cidade_id"),
                        ENDERECO.CEP,
                        Cidade.CIDADE.NOME.as("cidade_nome"),
                        Estado.ESTADO.ID.as("estado_id"),
                        Estado.ESTADO.NOME.as("estado_nome"),
                        Pais.PAIS.ID.as("pais_id"),
                        Pais.PAIS.NOME.as("pais_nome")
                )
                .from(Usuario.USUARIO)
                .leftJoin(UsuarioPerfil.USUARIO_PERFIL)
                .on(Usuario.USUARIO.ID.eq(UsuarioPerfil.USUARIO_PERFIL.USUARIO_ID))
                .leftJoin(PERFIL)
                .on(UsuarioPerfil.USUARIO_PERFIL.PERFIL_ID.eq(PERFIL.ID))
                .leftJoin(ENDERECO).on(Usuario.USUARIO.ID.eq(ENDERECO.USUARIO_ID))
                .leftJoin(Cidade.CIDADE).on(ENDERECO.CIDADE_ID.eq(Cidade.CIDADE.ID))
                .leftJoin(Estado.ESTADO).on(Cidade.CIDADE.ESTADO_ID.eq(Estado.ESTADO.ID))
                .leftJoin(Pais.PAIS).on(Estado.ESTADO.PAIS_ID.eq(Pais.PAIS.ID))
                .where(Usuario.USUARIO.ID.eq(id))
                .fetchOptional()
                .map(record -> {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setId(record.get(Usuario.USUARIO.ID));
                    usuario.setNome(record.get(Usuario.USUARIO.NOME));
                    usuario.setEmail(record.get(Usuario.USUARIO.EMAIL));
//                    usuario.setDataNascimento(convertLocalDateToString(record.get(Usuario.USUARIO.DATA_NASCIMENTO)));
                    usuario.setDataNascimento(record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                    if (record.get("perfil_id") != null) {
                        PerfilModel perfil = new PerfilModel();
                        perfil.setId(record.get("perfil_id", Integer.class));
                        perfil.setNome(record.get("perfil_nome", String.class));
                        usuario.setPerfis(new ArrayList<>(Collections.singleton(perfil)));
                    } else {
                        usuario.setPerfis(new ArrayList<>(Collections.emptySet()));
                    }
                    if (record.get("endereco_id") != null) {
                        EnderecoModel endereco = new EnderecoModel();
                        endereco.setId(record.get("endereco_id", Integer.class));
                        endereco.setLogradouro(record.get(ENDERECO.LOGRADOURO));
                        endereco.setNumero(record.get(ENDERECO.NUMERO));
                        endereco.setComplemento(record.get(ENDERECO.COMPLEMENTO));
                        endereco.setBairro(record.get(ENDERECO.BAIRRO));
                        endereco.setCep(record.get(ENDERECO.CEP));
                        if (record.get("cidade_id") != null && record.get("estado_id") != null && record.get("pais_id") != null) {
                            var estado = new EstadoModel();
                            estado.setId(record.get("estado_id", Integer.class));
                            estado.setNome(record.get("estado_nome", String.class));
                            var pais = new PaisModel();
                            pais.setId(record.get("pais_id", Integer.class));
                            estado.setPaisId(pais);
                            CidadeModel cidade = new CidadeModel();
                            cidade.setId(record.get("cidade_id", Integer.class));
                            cidade.setNome(record.get("cidade_nome", String.class));
                            cidade.setEstadoId(estado);
                            endereco.setCidadeId(cidade);
                        }
                        usuario.setEnderecos(new ArrayList<>(Collections.singleton(endereco)));
                    } else {
                        usuario.setEnderecos(new ArrayList<>(Collections.emptySet()));
                    }
                    return usuario;
                });
    }

    public Optional<UsuarioModel> findByEmail(String email) {
        return dsl.select(Usuario.USUARIO.fields())
                .select(USUARIO_PERFIL.PERFIL_ID)
                .select(PERFIL.NOME)
                .from(Usuario.USUARIO)
                .leftJoin(USUARIO_PERFIL).on(Usuario.USUARIO.ID.eq(USUARIO_PERFIL.USUARIO_ID))
                .leftJoin(PERFIL).on(USUARIO_PERFIL.PERFIL_ID.eq(PERFIL.ID))
                .where(Usuario.USUARIO.EMAIL.eq(email))
                .fetchOptional()
                .map(record -> {
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setId(record.get(Usuario.USUARIO.ID));
                    usuario.setNome(record.get(Usuario.USUARIO.NOME));
                    usuario.setEmail(record.get(Usuario.USUARIO.EMAIL));
                    usuario.setSenha(record.get(Usuario.USUARIO.SENHA));
//                    usuario.setDataNascimento(convertLocalDateToString(record.get(Usuario.USUARIO.DATA_NASCIMENTO)));
                    usuario.setDataNascimento(record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                    if (record.get(USUARIO_PERFIL.PERFIL_ID) != null) {
                        PerfilModel perfil = new PerfilModel();
                        perfil.setId(record.get(USUARIO_PERFIL.PERFIL_ID));
                        perfil.setNome(record.get(PERFIL.NOME));
                        List<PerfilModel> perfis = new ArrayList<>();
                        perfis.add(perfil);
                        usuario.setPerfis(perfis);
                    } else {
                        usuario.setPerfis(Collections.emptyList());
                    }
                    return usuario;
                });
    }

    public UsuarioModel create(UsuarioModel usuario) {
        if (isEmailExists(usuario)) {
            throw new EmailAlreadyExistsException("Email já existe: " + usuario.getEmail());
        }

        return dsl.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            String encryptedPassword = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(encryptedPassword);

            ctx.insertInto(Usuario.USUARIO)
                    .set(Usuario.USUARIO.NOME, usuario.getNome())
                    .set(Usuario.USUARIO.EMAIL, usuario.getEmail())
                    .set(Usuario.USUARIO.SENHA, usuario.getSenha())
//                    .set(Usuario.USUARIO.DATA_NASCIMENTO, convertStringToLocalDate(usuario.getDataNascimento()))
                    .set(Usuario.USUARIO.DATA_NASCIMENTO, usuario.getDataNascimento())
                    .execute();

            Integer userId = ctx.select(Usuario.USUARIO.ID)
                    .from(Usuario.USUARIO)
                    .where(Usuario.USUARIO.EMAIL.eq(usuario.getEmail()))
                    .fetchOneInto(Integer.class);
            usuario.setId(userId);

            saveUserProfiles(usuario, ctx);
            saveUserAddresses(usuario, ctx);

            return usuario;
        });
    }

    public UsuarioModel update(UsuarioModel usuarioModel) {
        return dsl.transactionResult(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            ctx.update(Usuario.USUARIO)
                    .set(Usuario.USUARIO.NOME, usuarioModel.getNome())
                    .set(Usuario.USUARIO.EMAIL, usuarioModel.getEmail())
                    .set(Usuario.USUARIO.DATA_NASCIMENTO, usuarioModel.getDataNascimento())
                    .where(Usuario.USUARIO.ID.eq(usuarioModel.getId()))
                    .execute();

            deletePerfilUsuarioById(usuarioModel.getId(), ctx);
            saveUserProfiles(usuarioModel, ctx);
            saveUserAddresses(usuarioModel, ctx);

            return findById(usuarioModel.getId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        });
    }

    private static void deletePerfilUsuarioById(Integer id, DSLContext ctx) {
        ctx.deleteFrom(USUARIO_PERFIL)
                .where(USUARIO_PERFIL.USUARIO_ID.eq(id))
                .execute();
    }

    public void deleteById(Integer id) {
        dsl.deleteFrom(Usuario.USUARIO).where(Usuario.USUARIO.ID.eq(id)).execute();
    }

    public void updatePassword(Integer id, String password) {
        String encryptedPassword = passwordEncoder.encode(password);
        dsl.update(Usuario.USUARIO)
                .set(Usuario.USUARIO.SENHA, encryptedPassword)
                .where(Usuario.USUARIO.ID.eq(id))
                .execute();
    }

    private boolean isEmailExists(UsuarioModel usuario) {
        return dsl.fetchExists(
                dsl.selectFrom(Usuario.USUARIO)
                        .where(DSL.lower(Usuario.USUARIO.EMAIL).eq(usuario.getEmail().toLowerCase()))
        );
    }

    private void saveUserProfiles(UsuarioModel usuario, DSLContext ctx) {
        for (PerfilModel perfil : usuario.getPerfis()) {
            ctx.insertInto(USUARIO_PERFIL)
                    .set(USUARIO_PERFIL.USUARIO_ID, usuario.getId())
                    .set(USUARIO_PERFIL.PERFIL_ID, perfil.getId())
                    .execute();
        }
    }

    private void saveUserAddresses(UsuarioModel usuario, DSLContext ctx) {
        for (EnderecoModel endereco : usuario.getEnderecos()) {
            if (isEnderecoExists(usuario, endereco, ctx)) {
                updateEnderecoUsuario(usuario, endereco, ctx);
            } else {
                insertEnderecoUsuario(usuario, endereco, ctx);
            }
        }
    }

    private boolean isEnderecoExists(UsuarioModel usuario, EnderecoModel endereco, DSLContext ctx) {
        return ctx.fetchExists(
                ctx.selectFrom(ENDERECO)
                        .where(ENDERECO.ID.eq(endereco.getId())
                                .and(ENDERECO.USUARIO_ID.eq(usuario.getId())))
        );
    }

    private void insertEnderecoUsuario(UsuarioModel usuario, EnderecoModel endereco, DSLContext ctx) {
//        Integer ufId = endereco.getUf() != null ? endereco.getUf().getId() : null;
        ctx.insertInto(ENDERECO)
                .set(ENDERECO.BAIRRO, endereco.getBairro())
                .set(ENDERECO.CEP, endereco.getCep())
                .set(ENDERECO.CIDADE_ID, endereco.getCidadeId().getId())
                .set(ENDERECO.LOGRADOURO, endereco.getLogradouro())
                .set(ENDERECO.USUARIO_ID, usuario.getId())
                .set(ENDERECO.NUMERO, endereco.getNumero())
                .set(ENDERECO.COMPLEMENTO, endereco.getComplemento())
                .execute();
    }

    private void updateEnderecoUsuario(UsuarioModel usuario, EnderecoModel endereco, DSLContext ctx) {
        ctx.update(ENDERECO)
                .set(ENDERECO.BAIRRO, endereco.getBairro())
                .set(ENDERECO.CEP, endereco.getCep())
                .set(ENDERECO.CIDADE_ID, endereco.getCidadeId().getId())
                .set(ENDERECO.LOGRADOURO, endereco.getLogradouro())
                .set(ENDERECO.NUMERO, endereco.getNumero())
                .set(ENDERECO.COMPLEMENTO, endereco.getComplemento())
                .where(ENDERECO.ID.eq(endereco.getId())
                        .and(ENDERECO.USUARIO_ID.eq(usuario.getId())))
                .execute();
    }
}