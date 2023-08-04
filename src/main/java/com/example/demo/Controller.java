package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// https://docs.openexchangerates.org/reference/latest-json
@RestController
public class Controller {
//    String APP_ID = "904e5bf25a2e4c19b51221173587d755";
//    private final String apiUrl = "https://openexchangerates.org/api/latest.json";
//
//    private final String historicalApiUrl = "https://openexchangerates.org/api/historical/";
//    private RestTemplate restTemplate;
//    private KafkaProducer kafkaProducer;
//    private SparkConsumer sparkConsumer;
//
//    @Autowired
//    public Controller(RestTemplate restTemplate, KafkaProducer kafkaProducer, SparkConsumer sparkConsumer) {
//        this.restTemplate = restTemplate;
//        this.kafkaProducer = kafkaProducer;
//        this.sparkConsumer = sparkConsumer;
//    }
//
//    @GetMapping("/exchange")
//    public Double getExchange() {
//        String url = apiUrl + "?app_id=" + APP_ID;
//        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
//
//        Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");
//        Double exchangeRateToGBP = rates.get("GBP");
//        return exchangeRateToGBP;
//    }
//
//    @GetMapping("/historicalExchange")
//    public List<Double> getHistoricalExchange() {
//
//        LocalDate endDate = LocalDate.now();
//        LocalDate startDate = endDate.minus(1, ChronoUnit.MONTHS);
//
//        List<Double> exchangeRates = new ArrayList<>();
//        LocalDate currentDate = startDate;
//
//        while (!currentDate.isAfter(endDate)) {
//            String url = historicalApiUrl + currentDate.toString() + ".json" + "?app_id=" + APP_ID;
//            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
//
//            Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");
//            Double exchangeRate = rates.get("GBP"); // Adjust this based on your API response structure
//            exchangeRates.add(exchangeRate);
//
//            currentDate = currentDate.plusDays(1);
//        }
//
//        kafkaProducer.sendExchangeRates(exchangeRates);
//
//        return exchangeRates;
//    }

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }

//    private final DeferredResult<Double> deferredResult = new DeferredResult<>();
//    @GetMapping("/start-consumer")
//    public DeferredResult<Double> startConsumer() throws InterruptedException {
//        sparkConsumer.consumeFromKafkaTopic(deferredResult);
//        return deferredResult;
//    }
}