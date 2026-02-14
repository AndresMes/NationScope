package com.example.nationscope.domain;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EconomicIndicators {

    private BigDecimal gdp;
    private Double growthRate;
    private Double inflation;
    private Double unemployment;
    private String currency;
    private Double publicDebt;
}
