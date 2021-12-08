package br.com.zupproject.pagamentoboletos.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class BoletoControllerTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}

	/*
	 * Casos de teste:
	 * 1 - caminho feliz com resposta da api = sucesso
	 * 2 - caminho feliz com resposta da api = falha
	 *     (aqui precisamos pensar. Vai gerar um estorno? temos que ver isso depois dos testes)
	 * 3 - a api estava indisponivel, então retornar 200 mas colocar o objeto em estado de aguardando pagamento
	 * 4 - erros do tipo 400 para:
	 *       * valor negativo
	 *     	 * codigo de barras em branco
	 *     	 * codigo de barras repetido para um boleto que ja foi pago
	 *     	 * objeto contaRequest nulo
	 *     	 * objeto contaRequest com email invalido
	 *     	 * objeto contaRequest com id de usuario não preenchido
	 * Totalizando no minimo 9 testes, mas podendo aumentar, se quiser granularizar todos os erros do tipo 400
	 */
}
