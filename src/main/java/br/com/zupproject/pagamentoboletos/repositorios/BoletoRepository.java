package br.com.zupproject.pagamentoboletos.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.zupproject.pagamentoboletos.entidades.Boleto;

public interface BoletoRepository extends JpaRepository<Boleto, Long> {

	@Query(value = "select * from boleto where pagamento = 'AGUARDANDO_PAGAMENTO'", nativeQuery = true)
	List<Boleto> findBoletosEmAberto();

}
