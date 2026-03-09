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

        String prompt = """
                You are a world-class expert in Economics and Sociology, trained at the most prestigious university in the world. Your task is to produce a **clear, professional executive report** analyzing the **socioeconomic stability and future outlook** of the country described below.
                Use **only the data provided** to support your analysis.
                
                IMPORTANT RULES:
                * If any value appears as **"Data not available"**, completely ignore it.
                * Do **not mention missing data** in the report.
                * Base your reasoning only on the available indicators.
                * Maintain a **professional and analytical tone**, similar to reports from international organizations.
                
                COUNTRY DATA
                
                GENERAL INFORMATION
                
                * Country: %s (Capital: %s)
                * Continent: %s
                * Area: %s km²
                * Population: %s inhabitants
                * Time zones: %s
                * Languages: %s
                * Currencies: %s
                
                ECONOMIC INDICATORS
                
                * GDP: $%s
                * Growth Rate: %s%%
                * Inflation: %s%%
                * Unemployment: %s%%
                * Public Debt: %s%% of GDP
                
                SOCIAL INDICATORS
                
                * Human Development Index (HDI): %s
                * Life Expectancy: %s years
                * Literacy Rate: %s%%
                * Poverty Rate: %s%%
                * Education Index: %s
                
                REPORT STRUCTURE
                
                1. **General Context**
                Provide a brief overview of the country’s current socioeconomic situation based on the available indicators.
                
                2. **Strengths and Weaknesses**
                Present key insights using bullet points:
                
                * Strengths
                * Weaknesses
                
                3. **Stability Score**
                Assign a stability score from **1 (very unstable) to 10 (very stable)** and briefly justify the rating.
                
                4. **Future Projections**
                Provide short-term and medium-term projections for both economic and social development.
                
                5. **Conclusion**
                Provide a concise final verdict summarizing the country’s stability and prospects based on the available data.
                
        """.formatted(
                format(countryData.name()),
                format(countryData.capital()),
                format(countryData.continents()),
                formatNumber(countryData.area(), "%,.2f"),
                formatNumber(countryData.population(), "%,d"),
                format(countryData.timeZones()),
                format(countryData.languages()),
                format(countryData.currencies()),

                // Economic Indicators
                format(countryData.economicIndicators().gdp()),
                formatNumber(countryData.economicIndicators().growthRate(), "%.2f"),
                formatNumber(countryData.economicIndicators().inflation(), "%.2f"),
                formatNumber(countryData.economicIndicators().unemployment(), "%.2f"),
                formatNumber(countryData.economicIndicators().publicDebt(), "%.2f"),


                formatNumber(countryData.socialIndicators().hdi(), "%.3f"),
                formatNumber(countryData.socialIndicators().lifeExpectancy(), "%.1f"),
                formatNumber(countryData.socialIndicators().literacyRate(), "%.2f"),
                formatNumber(countryData.socialIndicators().povertyRate(), "%.2f"),
                formatNumber(countryData.socialIndicators().educationIndex(), "%.3f")
        );

        GenerateContentResponse response = geminiClient.models.generateContent(
                "gemini-3-flash-preview",
                prompt,
                null
        );

        return new GeminiDTOResponse(response.text());

    }

    private String format(Object value) {
        return (value == null) ? "Data not available" : value.toString();
    }

    private String formatNumber(Number value, String pattern) {
        return (value == null) ? "Data not available" : String.format(pattern, value);
    }
}
