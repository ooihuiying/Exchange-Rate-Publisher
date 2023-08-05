package com.example.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SparkConsumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    private MessageHandler messageHandler;

    @Autowired
    public SparkConsumer(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @PostConstruct
    private void consumeFromKafkaTopic() throws InterruptedException {
        SparkConf sparkConf = new SparkConf().setAppName("KafkaSparkConsumer")
                // .setMaster("spark://spark:7077"); // Set this if we want to use Spark in Cluster mode
                .setMaster("local[*]"); // Set this if we want to use Local Spark ... need to modify docker accordingly..currently docker is configured to expect local spark
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));
        Map<String, Object> kafkaParams = new HashMap<>();

        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");

        JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(Collections.singleton("exchange-rates"), kafkaParams)
                );


        final AtomicReference<List<String>> allValues = new AtomicReference<>(new ArrayList<>());
        // Process each Kafka record's value and send to websocket
        stream.foreachRDD(rdd -> {
            List<String> batchValues = rdd.map(record -> {
                String value = record.value();
                System.out.println("Received exchange rate: " + value);
                return value;
            }).collect();
            System.out.println("Batch size: " + batchValues.size()); // Debug line
            batchValues.forEach(messageHandler::sendMessage);
        });

        streamingContext.start();

        // Register a shutdown hook to stop the streaming context when the application is closing
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Spark Streaming context...");
            streamingContext.stop(true, true); // Stop gracefully with wait and cleanup
        }));
    }
}

