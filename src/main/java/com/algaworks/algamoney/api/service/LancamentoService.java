package com.algaworks.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public Page<Lancamento> pesquisar(LancamentoFilter filtro, Pageable pageable) {
		return lancamentoRepository.filtrar(filtro, pageable);
	}

	public Lancamento buscar(Long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);

		if (!lancamento.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}

		return lancamento.get();
	}

	public Lancamento cadastrar(Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());

		if (!pessoa.isPresent()) {
			throw new PessoaInexistenteOuInativaException();
		}

		lancamento.setCodigo(null);
		return lancamentoRepository.save(lancamento);
	}

	public void remover(Long codigo) {
		Lancamento lancamento = this.buscar(codigo);
		lancamentoRepository.delete(lancamento);
	}
}
