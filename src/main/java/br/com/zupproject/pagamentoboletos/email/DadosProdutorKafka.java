package br.com.zupproject.pagamentoboletos.email;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DadosProdutorKafka {

	private String mensagem;
	private String assunto;
	private String remetente;
	private String destinatario;
	private String idMensagem = UUID.randomUUID().toString();

	private Boolean pagamentoComSucesso;
	private BigDecimal valor;
	private String nroConta;

	private String tipoOperacao = "BOLETO";
	private LocalDateTime dataOperacao = LocalDateTime.now();

	public DadosProdutorKafka(String mensagem, String assunto, String remetente, String destinatario,
			Boolean pagamentoComSucesso, BigDecimal valor, String nroConta) {
		this.mensagem = mensagem;
		this.assunto = assunto;
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.pagamentoComSucesso = pagamentoComSucesso;
		this.valor = valor;
		this.nroConta = nroConta;
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getAssunto() {
		return assunto;
	}

	public String getRemetente() {
		return remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public Boolean getPagamentoComSucesso() {
		return pagamentoComSucesso;
	}

	public String getIdMensagem() {
		return idMensagem;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public String getNroConta() {
		return nroConta;
	}

	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public LocalDateTime getDataOperacao() {
		return dataOperacao;
	}
}
