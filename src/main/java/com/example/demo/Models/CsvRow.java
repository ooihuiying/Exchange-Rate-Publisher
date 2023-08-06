package com.example.demo.Models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class CsvRow implements Serializable {
    private String currency;
    private String country;
    private Double value;
    private String date;
}
