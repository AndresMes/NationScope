package com.example.nationscope.service.impl;

import com.example.nationscope.dto.response.CountryDTOResponse;
import com.example.nationscope.dto.response.GeminiDTOResponse;
import com.example.nationscope.service.AnalyzeService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeServiceImpl implements AnalyzeService {

    private final Client geminiClient;

    @Override
    public GeminiDTOResponse analyzeCountry(CountryDTOResponse countryData) {

        GenerateContentResponse response = geminiClient.models.generateContent(
                "gemini-3-flash-preview",
                "Explain what is a prompt",
                null
        );

        return new GeminiDTOResponse(response.text());

    }
}
