package com.algaworks.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.pessoa.PessoaRepositoryQuery;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
