package br.com.zupproject.pagamentoboletos.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {

	@Query(value = "select * from boleto where status_pagamento = 'AGUARDANDO_PAGAMENTO' limit 1000", nativeQuery = true)
	List<Boleto> findBoletosEmAberto();

	List<Boleto> findAllByCodigoDeBarrasAndStatusPagamentoNot(String nrBoleto, StatusPagamento statusPagamento);

}
