package com.example.demo.Models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class ExchangeRateInfo implements Serializable {
    private String topic;
    private String value;
}
