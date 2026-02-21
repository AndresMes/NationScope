package com.example.nationscope.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record EconomicIndicatorsDTOResponse(
        BigDecimal gdp,
        Double growthRate,
        Double inflation,
        Double unemployment,
        Double publicDebt
) {
}
