package br.com.zupproject.pagamentoboletos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.zupproject.pagamentoboletos.controllers.requests.PagamentoRequest;
import br.com.zupproject.pagamentoboletos.controllers.responses.RespostaPagamentoBoleto;

@FeignClient(name = "pagamentoDeBoleto", url = "localhost:2040/api/boletos")
public interface PagamentoNoBancoClient {

	@PostMapping(consumes = "application/json")
	RespostaPagamentoBoleto realizarPagamento(@RequestBody PagamentoRequest request);
}

