package com.example.nationscope.dto.response;

import lombok.Builder;

@Builder
public record CountryAnalizedDTOResponse(
        CountryDTOResponse countryData,
        GeminiDTOResponse analysis
) {
}
