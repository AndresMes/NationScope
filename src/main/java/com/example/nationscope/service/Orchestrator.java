package com.example.nationscope.service;

import com.example.nationscope.dto.response.CountryAnalizedDTOResponse;
import com.example.nationscope.dto.response.CountryDTOResponse;

public interface Orchestrator {

    CountryAnalizedDTOResponse analyzeCountry(String countryName);
}
