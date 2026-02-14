package com.example.nationscope.dto.external;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CountryDtoRestCountries(
        NameDTO name,
        List<String> capital,
        List<String> continents,
        Double area,
        Long population,

        @JsonProperty("timezones")
        List<String> timeZones,

        Map<String, String> languages,
        Map<String, CurrencyDTO> currencies
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record NameDTO(String common) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrencyDTO(String name, String symbol) {}


}
