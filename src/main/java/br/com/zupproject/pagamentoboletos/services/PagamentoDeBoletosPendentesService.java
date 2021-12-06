package br.com.zupproject.pagamentoboletos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.zupproject.pagamentoboletos.clients.PagamentoNoBancoClient;
import br.com.zupproject.pagamentoboletos.commons.modelos.RespostaPagamento;
import br.com.zupproject.pagamentoboletos.controllers.requests.PagamentoRequest;
import br.com.zupproject.pagamentoboletos.controllers.responses.RespostaPagamentoBoleto;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import br.com.zupproject.pagamentoboletos.repositorios.BoletoRepository;
import feign.FeignException;

@Service
public class PagamentoDeBoletosPendentesService {

	@Autowired
	private BoletoRepository repository;
	
	@Autowired
	private PagamentoNoBancoClient client;
	
	@Scheduled(fixedDelayString = "${periodicidade.verificacao-boletos}")
	protected void tentaPagarBoletosEmAberto() {
		
		List<Boleto> boletosEmAberto = repository.findBoletosEmAberto();
		
		if(boletosEmAberto.isEmpty()) {
			return;
		}
		
		List<Boleto> boletosConferidos =boletosEmAberto.stream()
				.map(boleto -> tentaPagarBoleto(boleto))
				.collect(Collectors.toList());
		
		repository.saveAll(boletosConferidos);
	}

	private Boleto tentaPagarBoleto(Boleto boleto) {
		
		try {
			RespostaPagamentoBoleto respostaPagamento = client
					.realizarPagamento(new PagamentoRequest(boleto.getCodigoDeBarras(), boleto.getValor()));
			StatusPagamento statusPagamento = respostaPagamento.getResposta().equals(RespostaPagamento.SUCESSO)
					? StatusPagamento.PAGO
					: StatusPagamento.FALHA;
			boleto.setPagamento(statusPagamento);
		} catch (FeignException e) {
			boleto.setPagamento(StatusPagamento.FALHA);
		}
		
		return boleto;
	}
}
