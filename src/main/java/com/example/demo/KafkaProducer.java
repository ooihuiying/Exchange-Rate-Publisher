package com.example.demo;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, Double> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Double> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendExchangeRates(List<Double> exchangeRates) {
        for (Double rate : exchangeRates) {
            kafkaTemplate.sendDefault(rate);
        }
    }
}