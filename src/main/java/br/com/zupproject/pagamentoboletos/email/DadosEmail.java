package br.com.zupproject.pagamentoboletos.email;

import java.util.UUID;

public class DadosEmail {

	private String mensagem;
	private String assunto;
	private String remetente;
	private String destinatario;
	private Boolean pagamentoComSucesso;
	private String idMensagem = UUID.randomUUID().toString();

	public DadosEmail(String mensagem, String assunto, String remetente, String destinatario,
			Boolean pagamentoComSucesso) {
		this.mensagem = mensagem;
		this.assunto = assunto;
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.pagamentoComSucesso = pagamentoComSucesso;
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
}
