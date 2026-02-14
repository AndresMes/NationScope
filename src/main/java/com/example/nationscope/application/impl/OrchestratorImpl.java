package com.example.nationscope.application.impl;

import com.example.nationscope.application.Orchestrator;
import com.example.nationscope.dto.response.CountryDTOResponse;
import com.example.nationscope.service.AgreggationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrchestratorImpl implements Orchestrator {

    private final AgreggationService agreggationService;

    public CountryDTOResponse findCountryByName(String countryName){
        CountryDTOResponse countryDTOResponse = agreggationService.buildCountryEntity(countryName);

        return countryDTOResponse;
    }
}
