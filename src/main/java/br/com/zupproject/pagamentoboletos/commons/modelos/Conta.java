package br.com.zupproject.pagamentoboletos.commons.modelos;

import java.math.BigDecimal;

public class Conta {

	private Long idUsuario;
	private String nroConta;
	private BigDecimal saldo;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public String getNroConta() {
		return nroConta;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

}
