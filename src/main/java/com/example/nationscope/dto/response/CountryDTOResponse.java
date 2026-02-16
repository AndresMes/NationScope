package com.example.nationscope.dto.response;

import com.example.nationscope.domain.EconomicIndicators;
import com.example.nationscope.domain.SocialIndicators;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Builder
public record CountryDTOResponse(
        String name,
        List<String> capital,
        List<String>continents,
        Double area,
        Long population,
        List<String> timeZones,
        Map<String, String> languages,

        EconomicIndicators economicIndicators,
        SocialIndicators socialIndicators
) {
}
