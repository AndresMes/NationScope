package com.example.nationscope.service;

import com.example.nationscope.dto.response.CountryDTOResponse;
import com.example.nationscope.dto.response.GeminiDTOResponse;

public interface AnalyzeService {

    GeminiDTOResponse analyzeCountry(CountryDTOResponse countryData);
}
