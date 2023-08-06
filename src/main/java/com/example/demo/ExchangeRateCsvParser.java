package com.example.demo;

import com.example.demo.Models.CsvRow;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Include sleeping while parsing... to simulate events being produced in intervals
 */
@Service
public class ExchangeRateCsvParser {

    private KafkaProducer kafkaProducer;

    public ExchangeRateCsvParser(KafkaProducer kafkaProducer){
        this.kafkaProducer = kafkaProducer;
    }

    public void parseCsv(String currencyFilter) {
        int WAIT_AFTER_ROWS = 200;
        if (currencyFilter.isEmpty()) return;
        String csvFilePath = "exchange_rates.csv"; // Replace with the actual file path

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                ExchangeRateCsvParser.class.getClassLoader().getResourceAsStream(csvFilePath)))) {

            // Skip the first line (header)
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into fields using comma as the delimiter
                String[] fields = line.split(",");

                if (fields.length >= 5) {
                    int id = Integer.parseInt(fields[0].trim());
                    String country = fields[1].trim();
                    String currency = fields[2].trim();
                    double value = Double.parseDouble(fields[3].trim());
                    String date = fields[4].trim();

                    System.out.println("ID: " + id);
                    System.out.println("Country: " + country);
                    System.out.println("Currency: " + currency);
                    System.out.println("Value: " + value);
                    System.out.println("Date: " + date);
                    System.out.println("------------------------------");

                    if(currencyFilter.equals(currency)){
                        CsvRow newRow = CsvRow.builder().currency(currency).country(country).value(value).date(date).build();
                        this.kafkaProducer.sendExchangeRate(newRow);
                    }

                    if(id % WAIT_AFTER_ROWS == 0){
                        System.out.println("Sleeping ------------------------------");
                        Thread.sleep(3000);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
