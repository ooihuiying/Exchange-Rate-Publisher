//package com.example.demo;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.spark.SparkConf;
//import org.apache.spark.streaming.Durations;
//import org.apache.spark.streaming.api.java.JavaInputDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;
//import org.apache.spark.streaming.kafka010.ConsumerStrategies;
//import org.apache.spark.streaming.kafka010.KafkaUtils;
//import org.apache.spark.streaming.kafka010.LocationStrategies;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.request.async.DeferredResult;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//
//@Service
//public class SparkConsumer {
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String kafkaBootstrapServers;
//
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    public SparkConsumer(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    public void consumeFromKafkaTopic(DeferredResult<Double> deferredResult) throws InterruptedException {
//        SparkConf sparkConf = new SparkConf().setAppName("KafkaSparkConsumer");
//        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));
//
//        Map<String, Object> kafkaParams = new HashMap<>();
//        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
//        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//
//        JavaInputDStream<ConsumerRecord<String, String>> stream =
//                KafkaUtils.createDirectStream(
//                        streamingContext,
//                        LocationStrategies.PreferConsistent(),
//                        ConsumerStrategies.<String, String>Subscribe(Collections.singleton("exchange-rates"), kafkaParams)
//                );
//
//        stream.foreachRDD(rdd -> {
//            rdd.foreach(record -> {
//                String value = record.value();
//                // Process the message (exchange rate value) here
//                System.out.println("Received exchange rate: " + value);
//                messagingTemplate.convertAndSend("/topic/consumedValue", "{\"value\": " + value + "}");
//            });
//        });
//
//        streamingContext.start();
//        streamingContext.awaitTermination();
//    }
//}
//
