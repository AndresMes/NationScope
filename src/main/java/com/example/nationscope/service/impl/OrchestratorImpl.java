package com.example.nationscope.service.impl;

import com.example.nationscope.dto.response.CountryAnalizedDTOResponse;
import com.example.nationscope.dto.response.GeminiDTOResponse;
import com.example.nationscope.service.AnalyzeService;
import com.example.nationscope.service.Orchestrator;
import com.example.nationscope.dto.response.CountryDTOResponse;
import com.example.nationscope.service.AgreggationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrchestratorImpl implements Orchestrator {

    private final AgreggationService agreggationService;
    private final AnalyzeService analyzeService;

    public CountryAnalizedDTOResponse analyzeCountry(String countryName){

        CountryDTOResponse countryDTOResponse = buildCountryData(countryName);
        GeminiDTOResponse geminiDTOResponse = analyzeCountryWithAI(countryDTOResponse);

        return CountryAnalizedDTOResponse.builder()
                .countryData(countryDTOResponse)
                .analysis(geminiDTOResponse)
                .build();
    }

    private CountryDTOResponse buildCountryData(String countryName){
        return agreggationService.buildCountryEntity(countryName);
    }

    private GeminiDTOResponse analyzeCountryWithAI(CountryDTOResponse countryData){
        return analyzeService.analyzeCountry(countryData);
    }
}
