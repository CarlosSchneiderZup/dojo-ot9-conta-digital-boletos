package br.com.zupproject.pagamentoboletos.repositorios;

import java.util.List;
import java.util.Optional;

import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.zupproject.pagamentoboletos.entidades.Boleto;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {

	@Query(value = "select * from boleto where status_pagamento = 'AGUARDANDO_PAGAMENTO'", nativeQuery = true)
	List<Boleto> findBoletosEmAberto();

	@Query(value = "select * from boleto where codigo_de_barras = ':nrBoleto' and status_pagamento = 'FALHA'", nativeQuery = true)
	Optional<Boleto> findBoletoComPagamentoValido(String nrBoleto);


	List<Boleto> findAllByCodigoDeBarrasAndStatusPagamentoNot(String nrBoleto, StatusPagamento statusPagamento);


}
