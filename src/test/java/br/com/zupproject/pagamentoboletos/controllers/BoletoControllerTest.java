package br.com.zupproject.pagamentoboletos.controllers;

import static org.junit.jupiter.api.Assertions.*;

import br.com.zupproject.pagamentoboletos.clients.PagamentoNoBancoClient;
import br.com.zupproject.pagamentoboletos.commons.modelos.RespostaPagamento;
import br.com.zupproject.pagamentoboletos.controllers.requests.BoletoRequest;
import br.com.zupproject.pagamentoboletos.controllers.requests.ContaRequest;
import br.com.zupproject.pagamentoboletos.controllers.responses.RespostaPagamentoBoleto;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;
import br.com.zupproject.pagamentoboletos.entidades.embeddables.Conta;
import br.com.zupproject.pagamentoboletos.entidades.enums.StatusPagamento;
import br.com.zupproject.pagamentoboletos.repositorios.BoletoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BoletoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private PagamentoNoBancoClient bancoClient;

	@PersistenceContext
	private EntityManager entityManager;

	private String uri = "/boletos";

	@Test
	void deveRetornar200CasoApiBoletoSejaDevolvaSucesso() throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(100L),"12345465",new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void deveRetornar200CasoApiBoletoDevolvaFalha() throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(100L),"12345465",new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.FALHA));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void deveRetornar200EBoletoEmEstadoAguardandoPagamentoCasoAPIIndisponivel() throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(100L),"12345465",new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		FeignException feignException = Mockito.mock(FeignException.class);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenThrow(feignException);
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
		TypedQuery<Boleto> query = entityManager.createQuery("Select b from Boleto b where b.codigoDeBarras = :codigo and b.statusPagamento = :status",Boleto.class);
		query.setParameter("codigo",boleto.getCodigoDeBarras());
		query.setParameter("status", StatusPagamento.AGUARDANDO_PAGAMENTO);
		Optional<Boleto> boletoSalvo = query.getResultList().stream().findFirst();
		Assertions.assertTrue(boletoSalvo.isPresent());
	}
	@Test
	void deveRetornar400CasoValorNegativo() throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(-100L),"12345465",new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
	}
	@ParameterizedTest
	@CsvSource(value = {
			"null","''"
	},nullValues = {"null"},emptyValue = "")
	void deveRetornar400CasoCodigoDeBarrasEmBrancoOuNull(String codigoBarra) throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(100L),codigoBarra,new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	@Transactional
	void deveRetornar400CasoCodigoDeBarrasRepetidoEBoletoPago() throws Exception {
		String codigoBarra = "12334444";
		Boleto boleto = new Boleto(codigoBarra,BigDecimal.valueOf(100L),new Conta(1L,"123","teste@teste.com"));
		boleto.setPagamento(StatusPagamento.PAGO);
		entityManager.persist(boleto);
		BoletoRequest boletoRequest = new BoletoRequest(BigDecimal.valueOf(100L),codigoBarra,new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boletoRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void deveRetornar400CasoContaRequestSejaNull() throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(100L),"12345465",null);
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void deveRetornar400CasoContaRequestComEmailInvalido() throws Exception {
		BoletoRequest boletoRequest = new BoletoRequest(BigDecimal.valueOf(100L),"123456",new ContaRequest(1L,"123","banana"));
		String object = mapper.writeValueAsString(boletoRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void deveRetornar400CasoContaRequestComIdDeUsuarioNaoPreenchido() throws Exception {
		BoletoRequest boletoRequest = new BoletoRequest(BigDecimal.valueOf(100L),"123456",new ContaRequest(null,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boletoRequest);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void deveRetornar200EBoletoEmEstadoPagoCasoAPIRetorneSucesso() throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(100L),"12345465",new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		FeignException feignException = Mockito.mock(FeignException.class);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.SUCESSO));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
		TypedQuery<Boleto> query = entityManager.createQuery("Select b from Boleto b where b.codigoDeBarras = :codigo and b.statusPagamento = :status",Boleto.class);
		query.setParameter("codigo",boleto.getCodigoDeBarras());
		query.setParameter("status", StatusPagamento.PAGO);
		Optional<Boleto> boletoSalvo = query.getResultList().stream().findFirst();
		Assertions.assertTrue(boletoSalvo.isPresent());
	}

	@Test
	void deveRetornar200EBoletoEmEstadoFalhaCasoAPIRetorneFalha() throws Exception {
		BoletoRequest boleto = new BoletoRequest(BigDecimal.valueOf(100L),"12345465",new ContaRequest(1L,"123","teste@teste.com"));
		String object = mapper.writeValueAsString(boleto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(object);
		FeignException feignException = Mockito.mock(FeignException.class);
		Mockito.when(bancoClient.realizarPagamento(Mockito.any())).thenReturn(new RespostaPagamentoBoleto(RespostaPagamento.FALHA));
		mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
		TypedQuery<Boleto> query = entityManager.createQuery("Select b from Boleto b where b.codigoDeBarras = :codigo and b.statusPagamento = :status",Boleto.class);
		query.setParameter("codigo",boleto.getCodigoDeBarras());
		query.setParameter("status", StatusPagamento.FALHA);
		Optional<Boleto> boletoSalvo = query.getResultList().stream().findFirst();
		Assertions.assertTrue(boletoSalvo.isPresent());
	}
}
