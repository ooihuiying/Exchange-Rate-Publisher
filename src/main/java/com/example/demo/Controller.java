package com.example.demo;

import com.example.demo.Models.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RestController
public class Controller {

    private ExchangeRateCsvParser exchangeRateCsvParser;

    @Autowired
    public Controller(ExchangeRateCsvParser exchangeRateCsvParser) {
        this.exchangeRateCsvParser = exchangeRateCsvParser;
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

    // Can be called via endpoint or through UI at index.html
    @GetMapping("/loadFromCsv")
    @MessageMapping("/subscribe")
    @SendTo("/topic/rates")
    public void loadFromCsv(MessageDto messageDto){
        if (messageDto != null){
            this.exchangeRateCsvParser.parseCsv(messageDto.getMessage());
        }
    }
}