package br.com.zupproject.pagamentoboletos.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupproject.pagamentoboletos.clients.PagamentoNoBancoClient;
import br.com.zupproject.pagamentoboletos.commons.exceptions.ErroDeNegocioException;
import br.com.zupproject.pagamentoboletos.commons.modelos.RespostaPagamento;
import br.com.zupproject.pagamentoboletos.controllers.requests.BoletoRequest;
import br.com.zupproject.pagamentoboletos.controllers.requests.PagamentoRequest;
import br.com.zupproject.pagamentoboletos.controllers.responses.RespostaPagamentoBoleto;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import br.com.zupproject.pagamentoboletos.repositorios.BoletoRepository;
import feign.FeignException;

@RestController
@RequestMapping("/boletos")
public class BoletoController {

	@Autowired
	private BoletoRepository repository;

	@Autowired
	private PagamentoNoBancoClient client;

	@PostMapping
	public void pagarBoleto(@Valid @RequestBody BoletoRequest request) {

		if (request.getConta().getSaldo().compareTo(request.getValor()) < 0) {
			throw new ErroDeNegocioException(HttpStatus.UNPROCESSABLE_ENTITY,
					"Não há saldo suficiente para pagar um boleto no valor de " + request.getValor(), "Saldo");
		}

		Boleto boleto = request.toModel();

		try {

			RespostaPagamentoBoleto respostaPagamento = client
					.realizarPagamento(new PagamentoRequest(request.getCodigoDeBarras(), request.getValor()));
			StatusPagamento statusPagamento = respostaPagamento.getResposta().equals(RespostaPagamento.SUCESSO)
					? StatusPagamento.PAGO
					: StatusPagamento.FALHA;
			boleto.setPagamento(statusPagamento);

		} catch (FeignException e) {
			boleto.setPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO);
		}

		repository.save(boleto);
		
		// TODO - planejar o envio da resposta.
	}
}
