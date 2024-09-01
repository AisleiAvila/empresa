package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.Endereco;
import com.dasad.empresa.jooq.tables.UnidadeFederativa;
import com.dasad.empresa.jooq.tables.Usuario;
import com.dasad.empresa.models.EnderecoModel;
import com.dasad.empresa.models.UnidadeFederativaModel;
import com.dasad.empresa.models.UsuarioModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectFieldOrAsterisk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private static final Logger log = LogManager.getLogger(UsuarioRepositoryImpl.class);
    private final DSLContext dsl;

    @Autowired
    public UsuarioRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<UsuarioModel> findAll() {
        log.info("Executing findAll method");

        try {
            List<UsuarioModel> usuarios = this.dsl.selectFrom(Usuario.USUARIO).fetch((record) -> {
                UsuarioModel usuario = new UsuarioModel();
                usuario.setId((Integer)record.get(Usuario.USUARIO.ID));
                usuario.setNome((String)record.get(Usuario.USUARIO.NOME));
                usuario.setEmail((String)record.get(Usuario.USUARIO.EMAIL));
                usuario.setSenha((String)record.get(Usuario.USUARIO.SENHA));
                usuario.setDataNascimento((LocalDate)record.get(Usuario.USUARIO.DATA_NASCIMENTO));
                return usuario;
            });
            log.info("Successfully fetched {} users", usuarios.size());
            return usuarios;
        } catch (Exception var2) {
            Exception e = var2;
            log.error("Error fetching users: {}", e.getMessage());
            throw new RuntimeException("Error fetching users", e);
        }
    }

    public Optional<UsuarioModel> findById(Integer id) {
        UsuarioModel usuario = (UsuarioModel)this.dsl.selectFrom(Usuario.USUARIO).where(Usuario.USUARIO.ID.eq(id)).fetchOneInto(UsuarioModel.class);
        return Optional.ofNullable(usuario);
    }

    public UsuarioModel save(UsuarioModel usuario) {
        this.dsl.insertInto(Usuario.USUARIO).set(Usuario.USUARIO.NOME, usuario.getNome()).set(Usuario.USUARIO.EMAIL, usuario.getEmail()).set(Usuario.USUARIO.SENHA, usuario.getSenha()).set(Usuario.USUARIO.DATA_NASCIMENTO, usuario.getDataNascimento()).execute();
        return usuario;
    }

    public void deleteById(Integer id) {
        this.dsl.deleteFrom(Usuario.USUARIO).where(Usuario.USUARIO.ID.eq(id)).execute();
    }

    public Optional<UsuarioModel> findByEmail(String login) {
        Record record = this.dsl.select(new SelectFieldOrAsterisk[0]).from(Usuario.USUARIO).leftJoin(Endereco.ENDERECO).on(Usuario.USUARIO.ID.eq(Endereco.ENDERECO.USUARIO_ID)).leftJoin(UnidadeFederativa.UNIDADE_FEDERATIVA).on(Endereco.ENDERECO.UNIDADE_FEDERATIVA_ID.eq(UnidadeFederativa.UNIDADE_FEDERATIVA.ID)).where(Usuario.USUARIO.EMAIL.eq(login)).fetchOne();
        if (record == null) {
            return Optional.empty();
        } else {
            UnidadeFederativaModel unidadeFederativa = new UnidadeFederativaModel((Integer)record.get(Endereco.ENDERECO.UNIDADE_FEDERATIVA_ID), (String)record.get(UnidadeFederativa.UNIDADE_FEDERATIVA.NOME), (String)record.get(UnidadeFederativa.UNIDADE_FEDERATIVA.SIGLA));
            EnderecoModel endereco = new EnderecoModel((Integer)record.get(Endereco.ENDERECO.ID), (String)record.get(Endereco.ENDERECO.LOGRADOURO), (String)record.get(Endereco.ENDERECO.BAIRRO), (String)record.get(Endereco.ENDERECO.CIDADE), (String)record.get(Endereco.ENDERECO.ESTADO), (String)record.get(Endereco.ENDERECO.CEP), (UsuarioModel)null, unidadeFederativa);
            UsuarioModel usuario = new UsuarioModel((String)record.get(Usuario.USUARIO.NOME), (String)record.get(Usuario.USUARIO.EMAIL), (String)record.get(Usuario.USUARIO.SENHA), (LocalDate)record.get(Usuario.USUARIO.DATA_NASCIMENTO), (Set)null, (Set)null);
            usuario.setEnderecos(Collections.singleton(endereco));
            return Optional.of(usuario);
        }
    }
}
