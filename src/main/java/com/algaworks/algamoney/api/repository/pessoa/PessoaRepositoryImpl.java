package com.algaworks.algamoney.api.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.model.Pessoa_;
import com.algaworks.algamoney.api.repository.InternalRepository;
import com.algaworks.algamoney.api.repository.filter.PessoaFilter;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private InternalRepository internalRepository;

	@Override
	public Page<Pessoa> pesquisar(PessoaFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);

		Root<Pessoa> root = criteria.from(Pessoa.class);
		Predicate[] predicates = criarRestricoes(filtro, builder, root);

		criteria.where(predicates);

		TypedQuery<Pessoa> query = em.createQuery(criteria);

		Long total = internalRepository.getTotal(builder, root, predicates);
		return new PageImpl<>(query.getResultList(), pageable, total);
	}

	private Predicate[] criarRestricoes(PessoaFilter filtro, CriteriaBuilder builder, Root<Pessoa> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(root.get(Pessoa_.nome), "%".concat(filtro.getNome()).concat("%")));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
