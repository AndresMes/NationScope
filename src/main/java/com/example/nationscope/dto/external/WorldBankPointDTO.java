package com.example.nationscope.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorldBankPointDTO(

        BigDecimal value,
        String date
) {
}
