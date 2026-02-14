package com.example.nationscope.service;

import com.example.nationscope.dto.response.CountryDTOResponse;

public interface Orchestrator {

    CountryDTOResponse findCountryByName(String countryName);
}
