package com.example.demo;

import com.example.demo.Models.CsvRow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
public class KafkaProducer implements DisposableBean {

    private final String kafkaBootstrapServers;

    // TODO: Make this a bean
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final org.apache.kafka.clients.producer.KafkaProducer<String, String> kafkaProducer;

    @Autowired
    public KafkaProducer (@Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;

        // Create producer properties
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create a Kafka producer
        this.kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties);
    }
    public void sendExchangeRate(CsvRow row) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(row);
        // currency is the Kafka Topic
        ProducerRecord<String, String> record = new ProducerRecord<>(row.getCurrency(), json, json);
        this.kafkaProducer.send(record);
    }

    @Override
    public void destroy() throws Exception {
        this.kafkaProducer.close();
    }
}