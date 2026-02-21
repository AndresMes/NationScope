package com.example.nationscope.client;

import com.example.nationscope.dto.external.WorldBankPointDTO;
import com.example.nationscope.exception.DataNotAvailableForCountryWorldBankException;
import com.example.nationscope.exception.InvalidResponseWorldBankException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class WorldBankClient {

    private final RestClient worldBankApi;
    private final ObjectMapper objectMapper;

    private static final String GDP_INDICATOR = "NY.GDP.MKTP.CD";
    private static final String GROWTH_RATE_INDICATOR = "NY.GDP.MKTP.KD.ZG";
    private static final String INFLATION_INDICATOR = "FP.CPI.TOTL.ZG";
    private static final String UNEMPLOYMENT_INDICATOR = "SL.UEM.TOTL.ZS";
    private static final String PUBLIC_DEBT_INDICATOR = "GC.DOD.TOTL.GD.ZS";

    public WorldBankClient(@Qualifier("worldBankRestClient") RestClient worldBankApi, ObjectMapper objectMapper) {
        this.worldBankApi = worldBankApi;
        this.objectMapper = objectMapper;
    }

    public WorldBankPointDTO getGdpByCountry(String countryCode){
        return fetchWorldBankIndicator(countryCode, GDP_INDICATOR);
    }

    public WorldBankPointDTO getGrowthRateByCountry(String countryCode){
        return fetchWorldBankIndicator(countryCode, GROWTH_RATE_INDICATOR);
    }

    public WorldBankPointDTO getInflationByCountry(String countryCode){
        return fetchWorldBankIndicator(countryCode, INFLATION_INDICATOR);
    }

    public WorldBankPointDTO getUnempploymentIndicatorByCountry(String countryCode){
        return fetchWorldBankIndicator(countryCode, UNEMPLOYMENT_INDICATOR);
    }

    public WorldBankPointDTO getPublicDebtByCountry(String countryCode){
        return fetchWorldBankIndicator(countryCode, PUBLIC_DEBT_INDICATOR);
    }

    private WorldBankPointDTO fetchWorldBankIndicator(String countryCode, String indicator) {

        Object[] response = worldBankApi.get()
                .uri("/country/" + countryCode + "/indicator/" + indicator+"?format=json&per_page=1")
                .retrieve()
                .body(Object[].class);


        if (response == null || response.length < 2) {
            throw new InvalidResponseWorldBankException("No valid response for country: " + countryCode);
        }

        List<WorldBankPointDTO> list = objectMapper.convertValue(
                response[1],
                new TypeReference<List<WorldBankPointDTO>>() {}
        );

        if (list == null || list.isEmpty()) {
            throw new DataNotAvailableForCountryWorldBankException("No data available for this indicator for country: " + countryCode);
        }

        return list.get(0);
    }



}

