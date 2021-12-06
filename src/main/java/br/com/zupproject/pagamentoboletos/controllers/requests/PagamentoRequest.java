package br.com.zupproject.pagamentoboletos.controllers.requests;

import java.math.BigDecimal;

public class PagamentoRequest {

	private String nroBoleto;
	private BigDecimal valor;

	public PagamentoRequest(String nroBoleto, BigDecimal valor) {
		this.nroBoleto = nroBoleto;
		this.valor = valor;
	}

}
