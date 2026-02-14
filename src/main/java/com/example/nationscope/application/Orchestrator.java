package com.example.nationscope.application;

import com.example.nationscope.dto.response.CountryDTOResponse;

public interface Orchestrator {

    CountryDTOResponse findCountryByName(String countryName);
}
