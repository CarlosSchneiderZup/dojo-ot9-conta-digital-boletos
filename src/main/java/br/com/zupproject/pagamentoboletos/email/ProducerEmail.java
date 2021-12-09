package br.com.zupproject.pagamentoboletos.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerEmail {


        @Autowired
        private KafkaProperties kafkaProperties;

        private final Logger logger = LoggerFactory.getLogger(ProducerEmail.class);

        String topic = "transacoes";

        @Autowired
        private KafkaTemplate<String, DadosEmail> kafkaTemplate;

        public void enviarMensagem(DadosEmail t) {
            logger.info("Enviando Mensagem para o Kafka ");
            this.kafkaTemplate.send(topic, t);
        }
    }

