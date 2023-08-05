package com.example.demo;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class KafkaProducer {

    public void sendExchangeRates(List<Double> exchangeRates) {
        String bootstrapServers = "localhost:9092";

        // Create producer properties
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create a Kafka producer
        org.apache.kafka.clients.producer.KafkaProducer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties);
        for (Double rate : exchangeRates) {
            // kafkaTemplate.send("exchange-rates", String.valueOf(rate));
            String rateString = String.valueOf(rate);
            ProducerRecord<String, String> record = new ProducerRecord<>("exchange-rates", rateString, rateString);
            producer.send(record);
        }

        producer.close();
    }
}