package br.com.zupproject.pagamentoboletos.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerEmail {


        @Autowired
        private KafkaProperties kafkaProperties;

        String topic = "transacoes";

        @Autowired
        private KafkaTemplate<String, DadosEmail> kafkaTemplate;

        public void enviarMensagem(DadosEmail t) {
            this.kafkaTemplate.send(topic, t);
        }
    }

