package com.algaworks.algamoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.service.LancamentoService;

@Controller
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoService lancamentoService;

	@GetMapping
	public ResponseEntity<List<Lancamento>> listar() {
		return ResponseEntity.ok(lancamentoService.listar());
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscar(@PathVariable Long codigo) {
		return ResponseEntity.ok(lancamentoService.buscar(codigo));
	}

}
