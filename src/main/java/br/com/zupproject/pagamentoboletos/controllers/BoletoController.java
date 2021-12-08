package br.com.zupproject.pagamentoboletos.controllers;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zupproject.pagamentoboletos.clients.PagamentoNoBancoClient;
import br.com.zupproject.pagamentoboletos.commons.modelos.RespostaPagamento;
import br.com.zupproject.pagamentoboletos.controllers.requests.BoletoRequest;
import br.com.zupproject.pagamentoboletos.controllers.requests.PagamentoRequest;
import br.com.zupproject.pagamentoboletos.controllers.responses.RespostaPagamentoBoleto;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import br.com.zupproject.pagamentoboletos.repositorios.BoletoRepository;
import br.com.zupproject.pagamentoboletos.services.EmailService;
import feign.FeignException;

@RestController
@RequestMapping("/boletos")
public class BoletoController {

	@Autowired
	private BoletoRepository repository;

	@Autowired
	private PagamentoNoBancoClient client;

	@Autowired
	private EmailService email;

	private final Logger logger = LoggerFactory.getLogger(BoletoController.class);

	@PostMapping
	public void pagarBoleto(@Valid @RequestBody BoletoRequest request) {

		Boleto boleto = request.toModel();

		try {

			RespostaPagamentoBoleto respostaPagamento = client
					.realizarPagamento(new PagamentoRequest(request.getCodigoDeBarras(), request.getValor()));
			StatusPagamento statusPagamento = respostaPagamento.getResposta().equals(RespostaPagamento.SUCESSO)
					? StatusPagamento.PAGO
					: StatusPagamento.FALHA;
			boleto.setPagamento(statusPagamento);

			preparaListenerEmail(boleto);

		} catch (FeignException e) {
			logger.warn("Serviço de pagamento indisponível em " + LocalDateTime.now());
			boleto.setPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO);
		}

		repository.save(boleto);
	}

	private void preparaListenerEmail(Boleto boleto) {

		if (boleto.getStatusPagamento().equals(StatusPagamento.PAGO)) {
			email.pagamentoSucesso(boleto);
		} else {
			email.pagamentoSemSucesso(boleto);
		}

	}
}
