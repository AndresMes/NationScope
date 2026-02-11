package com.example.nationscope.dto.external;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CountryDtoRestCountries(
        NameDTO name,
        List<String> capital,
        List<String> continents,
        Double area,
        Long population,

        @JsonProperty("timezones")
        List<TimeZone> timeZones,

        Map<String, String> languages,
        Map<String, CurrencyDTO> currencies
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record NameDTO(String common) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrencyDTO(String name, String symbol) {}


    public String getFirstCapital() {
        return (capital != null && !capital.isEmpty()) ? capital.get(0) : "N/A";
    }

    public String getFirstLanguage() {
        return (languages != null && !languages.isEmpty())
                ? languages.values().iterator().next()
                : "N/A";
    }
}
