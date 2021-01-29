package com.algaworks.algamoney.api.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

@Repository
public class InternalRepository {

	@PersistenceContext
	private EntityManager em;

	public Long getTotal(CriteriaBuilder builder, Root<?> root, Predicate[] predicates) {
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<?> rootAux = criteria.from(root.getJavaType());

		criteria.select(builder.count(rootAux));
		criteria.where(predicates);

		TypedQuery<Long> typedQuery = em.createQuery(criteria);
		return typedQuery.getSingleResult();
	}

}
