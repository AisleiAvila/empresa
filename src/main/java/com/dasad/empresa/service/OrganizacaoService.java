package com.dasad.empresa.service;

import com.dasad.empresa.model.OrganizacaoModel;
import com.dasad.empresa.model.OrganizacaoRequest;
import com.dasad.empresa.repository.OrganizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar operações relacionadas a organizações.
 * Este serviço interage com o repositório {@link OrganizacaoRepository} para
 * realizar operações de CRUD (Create, Read, Update, Delete) em entidades {@link OrganizacaoModel}.
 */
@Service
public class OrganizacaoService {

    /**
     * Repositório de organizações injetado automaticamente pelo Spring.
     */
    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    /**
     * Construtor padrão da classe.
     */
    public OrganizacaoService() {
    }

    /**
     * Busca uma lista de organizações com base em uma solicitação específica.
     *
     * @param organizacaoRequest Objeto {@link OrganizacaoRequest} que contém os critérios de busca.
     * @return Um {@link Optional} contendo a lista de organizações encontradas ou vazio se nenhuma for encontrada.
     */
    public Optional<List<OrganizacaoModel>> find(OrganizacaoRequest organizacaoRequest) {
        return this.organizacaoRepository.find(organizacaoRequest);
    }

    /**
     * Conta o número total de registros de organizações que correspondem a uma solicitação específica.
     *
     * @param organizacaoRequest Objeto {@link OrganizacaoRequest} que contém os critérios de busca.
     * @return Um {@link Optional} contendo o número total de registros ou vazio se nenhum for encontrado.
     */
    public Optional<Integer> countTotalRecords(OrganizacaoRequest organizacaoRequest) {
        return this.organizacaoRepository.countTotalRecords(organizacaoRequest);
    }

    /**
     * Busca uma organização pelo seu ID.
     *
     * @param id O ID da organização a ser buscada.
     * @return Um {@link Optional} contendo a organização encontrada ou vazio se nenhuma for encontrada.
     */
    public Optional<OrganizacaoModel> findById(Integer id) {
        return this.organizacaoRepository.findById(id);
    }

    /**
     * Cria uma nova organização.
     *
     * @param organizacao Objeto {@link OrganizacaoModel} que representa a organização a ser criada.
     * @return O objeto {@link OrganizacaoModel} criado.
     */
    public OrganizacaoModel create(OrganizacaoModel organizacao) {
        return this.organizacaoRepository.create(organizacao);
    }

    /**
     * Atualiza uma organização existente.
     *
     * @param organizacao Objeto {@link OrganizacaoModel} que representa a organização a ser atualizada.
     * @return O objeto {@link OrganizacaoModel} atualizado.
     */
    public OrganizacaoModel update(OrganizacaoModel organizacao) {
        return this.organizacaoRepository.update(organizacao);
    }

    /**
     * Exclui uma organização pelo seu ID.
     *
     * @param id O ID da organização a ser excluída.
     */
    public void deleteById(Integer id) {
        this.organizacaoRepository.deleteById(id);
    }
}