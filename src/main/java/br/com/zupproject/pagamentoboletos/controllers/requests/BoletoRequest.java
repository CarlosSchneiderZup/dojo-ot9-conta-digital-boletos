package br.com.zupproject.pagamentoboletos.controllers.requests;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.zupproject.pagamentoboletos.commons.modelos.Conta;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;

public class BoletoRequest {

	@NotNull
	@Positive
	private BigDecimal valor;

	@NotBlank
	private String codigoDeBarras;

	@NotNull
	private Conta conta;

	public BigDecimal getValor() {
		return valor;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public Conta getConta() {
		return conta;
	}

	public Boleto toModel() {
		return new Boleto(codigoDeBarras, valor, conta.getIdUsuario());
	}
	
	
}
