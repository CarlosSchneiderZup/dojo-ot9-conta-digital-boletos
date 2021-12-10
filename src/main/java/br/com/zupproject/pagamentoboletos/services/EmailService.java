package br.com.zupproject.pagamentoboletos.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zupproject.pagamentoboletos.email.DadosProdutorKafka;
import br.com.zupproject.pagamentoboletos.email.ProducerEmail;
import br.com.zupproject.pagamentoboletos.entidades.Boleto;

@Service
public class EmailService {

    @Autowired
    private ProducerEmail envio;


    public void pagamentoSucesso(Boleto boleto){
        String assunto = "Pagamento efetuado com sucesso";
        String mensagem = "Pagamento do boleto com código de barras " + boleto.getCodigoDeBarras();
        String remetente = "nao.resposand@otbank.com.br";
        String destinatario = boleto.getConta().getEmail();
        BigDecimal valor = boleto.getValor();
        String nroConta = boleto.getConta().getNroConta();
        DadosProdutorKafka dadosEmail = new DadosProdutorKafka(mensagem,assunto,remetente,destinatario, true, valor, nroConta);
        envio.enviarMensagem(dadosEmail);
    }


    public void pagamentoSemSucesso(Boleto boleto){
        String assunto = "Falha ao efetuar pagamento";
        String mensagem = "Falha ao efetuar pagamento do boleto " + boleto.getCodigoDeBarras();
        String remetente = "nao.resposand@otbank.com.br";
        String destinatario = boleto.getConta().getEmail();
        BigDecimal valor = boleto.getValor();
        String nroConta = boleto.getConta().getNroConta();
        DadosProdutorKafka dadosEmail = new DadosProdutorKafka(assunto,mensagem,remetente,destinatario, false, valor, nroConta);
        envio.enviarMensagem(dadosEmail);
    }

}
