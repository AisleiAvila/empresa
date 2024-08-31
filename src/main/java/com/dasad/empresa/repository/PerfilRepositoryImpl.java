package com.dasad.empresa.repository;

import com.dasad.empresa.jooq.tables.Perfis;
import com.dasad.empresa.models.PerfilModel;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class PerfilRepositoryImpl implements PerfilRepository {
    private static final Logger log = LogManager.getLogger(PerfilRepositoryImpl.class);
    private final DSLContext dsl;

    public PerfilRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<PerfilModel> findAll() {
        log.info("Executando o método findAll");

        try {
            List<PerfilModel> perfis = this.dsl.selectFrom(Perfis.PERFIS).fetch((record) -> {
                PerfilModel perfil = new PerfilModel();
                perfil.setId((Integer) record.get(Perfis.PERFIS.ID));
                perfil.setNome((String) record.get(Perfis.PERFIS.NOME));
                return perfil;
            });
            log.info("Buscado com sucesso {} perfis", perfis.size());
            return perfis;
        } catch (Exception var2) {
            Exception e = var2;
            log.error("Erro ao buscar perfis: {}", e.getMessage());
            throw new RuntimeException("Erro ao buscar perfis", e);
        }
    }
}