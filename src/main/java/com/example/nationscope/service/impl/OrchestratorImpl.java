package com.example.nationscope.service.impl;

import com.example.nationscope.service.Orchestrator;
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
