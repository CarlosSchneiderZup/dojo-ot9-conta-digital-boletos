package br.com.zupproject.pagamentoboletos.controllers.responses;

import br.com.zupproject.pagamentoboletos.commons.modelos.RespostaPagamento;

public class RespostaPagamentoBoleto {

	private RespostaPagamento resposta;


	@Deprecated
	public RespostaPagamentoBoleto() {
	}

	public RespostaPagamentoBoleto(RespostaPagamento resposta) {
		this.resposta = resposta;
	}

	public RespostaPagamento getResposta() {
		return resposta;
	}
}
