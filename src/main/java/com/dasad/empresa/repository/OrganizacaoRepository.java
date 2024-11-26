package com.dasad.empresa.repository;

import com.dasad.empresa.model.OrganizacaoModel;
import com.dasad.empresa.model.OrganizacaoRequest;

import java.util.List;
import java.util.Optional;

public interface OrganizacaoRepository {
    Optional<List<OrganizacaoModel>> find(OrganizacaoRequest organizacaorequest);
    Optional<Integer> countTotalRecords(OrganizacaoRequest organizacaorequest);
    Optional<OrganizacaoModel> findById(Integer id);
    OrganizacaoModel create(OrganizacaoModel organizacao);
    OrganizacaoModel update(OrganizacaoModel organizacao);
    void deleteById(Integer id);
}
