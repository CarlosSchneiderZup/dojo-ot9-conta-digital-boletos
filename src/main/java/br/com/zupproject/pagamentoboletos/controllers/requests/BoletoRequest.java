package br.com.zupproject.pagamentoboletos.controllers.requests;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.zupproject.pagamentoboletos.commons.validator.Pagamento;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;

public class BoletoRequest {

	@NotNull
	@Positive
	private BigDecimal valor;

	@NotBlank
	@Pagamento(domainClass = Boleto.class, fieldName = "codigoDeBarras")
	private String codigoDeBarras;

	@NotNull @Valid
	private ContaRequest contaRequest;

	@Deprecated
	public BoletoRequest() {
	}

	public BoletoRequest(BigDecimal valor, String codigoDeBarras, ContaRequest contaRequest) {
		this.valor = valor;
		this.codigoDeBarras = codigoDeBarras;
		this.contaRequest = contaRequest;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public ContaRequest getContaRequest() {
		return contaRequest;
	}

	public Boleto toModel() {
		return new Boleto(codigoDeBarras, valor, contaRequest.toModel());
	}

}
