package com.algaworks.algamoney.api.repository.lancamento;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.algaworks.algamoney.api.model.Categoria_;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Lancamento_;
import com.algaworks.algamoney.api.model.Pessoa_;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter filtro, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteriaQuery = criteriaBuilder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(filtro, criteriaBuilder, root);
		criteriaQuery.select(root);
		criteriaQuery.where(predicates);

		TypedQuery<Lancamento> lancamentos = em.createQuery(criteriaQuery);
		adicionarRestricoesDePaginacao(lancamentos, pageable);

		return new PageImpl<>(lancamentos.getResultList(), pageable, getTotal(filtro));
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class, 
				root.get(Lancamento_.codigo),
				root.get(Lancamento_.descricao),
				root.get(Lancamento_.dataVencimento),
				root.get(Lancamento_.dataPagamento),
				root.get(Lancamento_.valor),
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.categoria).get(Categoria_.nome),
				root.get(Lancamento_.pessoa).get(Pessoa_.nome)
			));
		
		Predicate[] predicates = criarRestricoes(filtro, builder, root);
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = em.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, getTotal(filtro));
}

	private Predicate[] criarRestricoes(LancamentoFilter filtro, CriteriaBuilder criteriaBuilder,
			Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Lancamento_.descricao)),
					"%" + filtro.getDescricao().toLowerCase() + "%"));
		}

		if (filtro.getDataVencimentoDe() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					filtro.getDataVencimentoDe()));
		}

		if (filtro.getDataVencimentoAte() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					filtro.getDataVencimentoAte()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> lancamentos, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = (paginaAtual * totalRegistrosPorPagina);

		lancamentos.setFirstResult(primeiroRegistroDaPagina);
		lancamentos.setMaxResults(totalRegistrosPorPagina);
	}

	private Long getTotal(LancamentoFilter filtro) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		criteria.select(builder.count(root));
		criteria.where(criarRestricoes(filtro, builder, root));

		TypedQuery<Long> typedQuery = em.createQuery(criteria);
		return typedQuery.getSingleResult();
	}

}
