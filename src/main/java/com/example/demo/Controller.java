package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// https://docs.openexchangerates.org/reference/latest-json
@RestController
public class Controller {
    String APP_ID = "904e5bf25a2e4c19b51221173587d755"; // APP ID not working anymore
    private final String apiUrl = "https://openexchangerates.org/api/latest.json";

    private final String historicalApiUrl = "https://openexchangerates.org/api/historical/";
    private RestTemplate restTemplate;
    private KafkaProducer kafkaProducer;

    private MessageHandler messageHandler;

    @Autowired
    public Controller(RestTemplate restTemplate, KafkaProducer kafkaProducer, MessageHandler messageHandler) {
        this.restTemplate = restTemplate;
        this.kafkaProducer = kafkaProducer;
        this.messageHandler = messageHandler;
    }

    @GetMapping("/exchange")
    public Double getExchange() {
        String url = apiUrl + "?app_id=" + APP_ID;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        Map<String, Double> rates = (Map<String, Double>) response.getBody().get("rates");
        Double exchangeRateToGBP = rates.get("GBP");
        return exchangeRateToGBP;
    }

    @GetMapping("/historicalExchange")
    public List<Double> getHistoricalExchange() {

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
        List<Double> exchangeRates = Arrays.asList(
                0.786287, 0.787126, 0.78475, 0.778907, 0.778907, 0.77894, 0.777361, 0.773156, 0.769468,
                0.76171, 0.763825, 0.763884, 0.763961, 0.764994, 0.767241, 0.772963, 0.776817, 0.777847,
                0.777847, 0.778142, 0.780511, 0.775573, 0.773572, 0.781657, 0.778452, 0.778452, 0.778003,
                0.779317, 0.78154, 0.786201, 0.786431, 0.78357
        );


        kafkaProducer.sendExchangeRates(exchangeRates);

        return exchangeRates;
    }

    // Return index.html
    @GetMapping("/")
    public String index(Model model) throws IOException {
        Resource resource = new ClassPathResource("templates/index.html");
        InputStream inputStream = resource.getInputStream();

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }

        String htmlContent = contentBuilder.toString();

        return htmlContent;
    }
}