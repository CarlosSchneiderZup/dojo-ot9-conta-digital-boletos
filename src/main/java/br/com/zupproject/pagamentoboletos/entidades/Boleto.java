package br.com.zupproject.pagamentoboletos.entidades;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.zupproject.pagamentoboletos.entidades.embeddables.Conta;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;

@Entity
public class Boleto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private String codigoDeBarras;
	@Column(nullable = false)
	private BigDecimal valor;
	@Embedded
	private Conta conta;

	@Enumerated(EnumType.STRING)
	private StatusPagamento statusPagamento;

	@Deprecated
	public Boleto() {
	}

	public Boleto(String codigoDeBarras, BigDecimal valor, Conta conta) {
		this.codigoDeBarras = codigoDeBarras;
		this.valor = valor;
		this.conta = conta;
	}

	public void setPagamento(StatusPagamento pagamento) {
		this.statusPagamento = pagamento;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}

	public Conta getConta() {
		return conta;
	}
}
