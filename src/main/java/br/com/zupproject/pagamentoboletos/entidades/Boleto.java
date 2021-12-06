package br.com.zupproject.pagamentoboletos.entidades;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;

@Entity
public class Boleto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false, unique = true)
	private String codigoDeBarras;
	@Column(nullable = false)
	private BigDecimal valor;
	@Column(nullable = false)
	private Long idCliente;

	@Enumerated(EnumType.STRING)
	private StatusPagamento pagamento;

	@Deprecated
	public Boleto() {
	}

	public Boleto(String nroBoleto, BigDecimal valor, Long idCliente) {
		super();
		this.codigoDeBarras = nroBoleto;
		this.valor = valor;
		this.idCliente = idCliente;
	}

	public void setPagamento(StatusPagamento pagamento) {
		this.pagamento = pagamento;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public BigDecimal getValor() {
		return valor;
	}

}
