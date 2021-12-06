package br.com.zupproject.pagamentoboletos.commons.exceptions;

import org.springframework.http.HttpStatus;

public class ErroDeNegocioException extends RuntimeException {

	private HttpStatus status;
	private String mensagem;
	private String campo;

	public ErroDeNegocioException(HttpStatus status, String mensagem, String campo) {
		super(mensagem);
		this.status = status;
		this.campo = campo;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getCampo() {
		return campo;
	}

}
