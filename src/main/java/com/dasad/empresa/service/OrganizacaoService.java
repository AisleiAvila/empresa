package com.dasad.empresa.service;

import com.dasad.empresa.model.OrganizacaoModel;
import com.dasad.empresa.model.OrganizacaoRequest;
import com.dasad.empresa.repository.OrganizacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizacaoService {
    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    public OrganizacaoService() {
    }

    public Optional<List<OrganizacaoModel>> find(OrganizacaoRequest organizacaoRequest) {
        return this.organizacaoRepository.find(organizacaoRequest);
    }

    public Optional<Integer> countTotalRecords(OrganizacaoRequest organizacaoRequest) {
        return this.organizacaoRepository.countTotalRecords(organizacaoRequest);
    }

    public Optional<OrganizacaoModel> findById(Integer id) {
        return this.organizacaoRepository.findById(id);
    }

    public OrganizacaoModel create(OrganizacaoModel organizacao) {
        return this.organizacaoRepository.create(organizacao);
    }

    public OrganizacaoModel update(OrganizacaoModel organizacao) {
        return this.organizacaoRepository.update(organizacao);
    }

    public void deleteById(Integer id) {
        this.organizacaoRepository.deleteById(id);
    }

}
