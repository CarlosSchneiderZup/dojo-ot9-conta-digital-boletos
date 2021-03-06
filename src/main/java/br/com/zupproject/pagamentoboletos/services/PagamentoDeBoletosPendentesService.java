package br.com.zupproject.pagamentoboletos.services;

import br.com.zupproject.pagamentoboletos.clients.PagamentoNoBancoClient;
import br.com.zupproject.pagamentoboletos.commons.modelos.RespostaPagamento;
import br.com.zupproject.pagamentoboletos.controllers.requests.PagamentoRequest;
import br.com.zupproject.pagamentoboletos.controllers.responses.RespostaPagamentoBoleto;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import br.com.zupproject.pagamentoboletos.repositorios.BoletoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagamentoDeBoletosPendentesService {

    private final Logger logger = LoggerFactory.getLogger(PagamentoDeBoletosPendentesService.class);

    @Autowired
    private BoletoRepository repository;

    @Autowired
    private PagamentoNoBancoClient client;

    @Autowired
    private EmailService email;


    @Scheduled(fixedDelayString = "${periodicidade.verificacao-boletos}")
    protected void tentaPagarBoletosEmAberto() {
        logger.info("Inicio da rotina para reprocessamento de boletos pendentes");
        List<Boleto> boletosEmAberto = repository.findBoletosEmAberto();
        logger.info("{} Boletos encontrados",boletosEmAberto.size());
        if (boletosEmAberto.isEmpty()) {
            return;
        }

        List<Boleto> boletosConferidos = boletosEmAberto.stream()
                .map(this::tentaPagarBoleto)
                .collect(Collectors.toList());

        repository.saveAll(boletosConferidos);
    }

    private Boleto tentaPagarBoleto(Boleto boleto) {
        logger.info("Tentando Pagar Boleto {}",boleto.getCodigoDeBarras());
        try {
            RespostaPagamentoBoleto respostaPagamento = client
                    .realizarPagamento(new PagamentoRequest(boleto.getCodigoDeBarras(), boleto.getValor()));
            StatusPagamento statusPagamento = respostaPagamento.getResposta().equals(RespostaPagamento.SUCESSO)
                    ? StatusPagamento.PAGO
                    : StatusPagamento.FALHA;
            boleto.setPagamento(statusPagamento);
            preparaListenerEmail(boleto);
        } catch (FeignException e) {
            logger.error("Servi??o de pagamento de boletos indispon??vel, cancelando boleto de n??mero {}",boleto.getCodigoDeBarras());
            boleto.setPagamento(StatusPagamento.FALHA);
			preparaListenerEmail(boleto);
        }

        return boleto;
    }


    private void preparaListenerEmail(Boleto boleto) {

        if (boleto.getStatusPagamento().equals(StatusPagamento.PAGO)) {
            logger.info("Boleto {} Pago",boleto.getCodigoDeBarras());
            email.pagamentoSucesso(boleto);
        } else {
            logger.info("Falha ao pagar Boleto {}", boleto.getCodigoDeBarras());
            email.pagamentoSemSucesso(boleto);
        }
    }

}
