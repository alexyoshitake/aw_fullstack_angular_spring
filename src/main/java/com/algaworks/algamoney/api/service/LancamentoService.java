package com.algaworks.algamoney.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Lancamento> listar() {
		return lancamentoRepository.findAll();
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
}
