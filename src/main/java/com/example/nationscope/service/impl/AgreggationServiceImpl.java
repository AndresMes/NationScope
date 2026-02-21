package com.example.nationscope.service.impl;

import com.example.nationscope.client.CountriesClient;
import com.example.nationscope.client.WorldBankClient;
import com.example.nationscope.domain.EconomicIndicators;
import com.example.nationscope.domain.SocialIndicators;
import com.example.nationscope.dto.external.CountryDtoRestCountries;
import com.example.nationscope.dto.external.WorldBankPointDTO;
import com.example.nationscope.dto.response.CountryDTOResponse;
import com.example.nationscope.dto.response.EconomicIndicatorsDTOResponse;
import com.example.nationscope.service.AgreggationService;
import com.example.nationscope.utils.CountryCodeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreggationServiceImpl implements AgreggationService {

    private final CountriesClient countriesClient;
    private final WorldBankClient worldBankClient;
    private final CountryCodeConverter countryCodeConverter;

    @Override
    public CountryDTOResponse buildCountryEntity(String countryName) {
        if (countryName == null || countryName.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }

        CountryDtoRestCountries restCountriesResponse = countriesClient.getCountryByName(countryName.toLowerCase());

        EconomicIndicatorsDTOResponse ecoDto = buildEconomicIndicators(countryName);

        //Refactor to a proper mapper soon
        EconomicIndicators ecoDomain = mapToDomain(ecoDto);

        return CountryDTOResponse.builder()
                .name(restCountriesResponse.name().common())
                .capital(restCountriesResponse.capital() != null ? restCountriesResponse.capital() : List.of())
                .continents(restCountriesResponse.continents())
                .area(restCountriesResponse.area())
                .population(restCountriesResponse.population())
                .economicIndicators(ecoDomain)
                .currencies(restCountriesResponse.currencies())
                .languages(restCountriesResponse.languages())
                .build();
    }

    private EconomicIndicatorsDTOResponse buildEconomicIndicators(String country) {
        String isoCode = countryCodeConverter.convertToIso(country);

        return EconomicIndicatorsDTOResponse.builder()
                .gdp(safeGetBigDecimal(worldBankClient.getGdpByCountry(isoCode)))
                .growthRate(safeGetDouble(worldBankClient.getGrowthRateByCountry(isoCode)))
                .inflation(safeGetDouble(worldBankClient.getInflationByCountry(isoCode)))
                .unemployment(safeGetDouble(worldBankClient.getUnempploymentIndicatorByCountry(isoCode)))
                .publicDebt(safeGetDouble(worldBankClient.getPublicDebtByCountry(isoCode)))
                .build();
    }

    private Double safeGetDouble(WorldBankPointDTO dto) {
        return (dto != null && dto.value() != null) ? dto.value().doubleValue() : 0.0;
    }

    private BigDecimal safeGetBigDecimal(WorldBankPointDTO dto) {
        return (dto != null && dto.value() != null) ? dto.value() : BigDecimal.ZERO;
    }

    private EconomicIndicators mapToDomain(EconomicIndicatorsDTOResponse dto) {
        return EconomicIndicators.builder()
                .gdp(dto.gdp())
                .growthRate(dto.growthRate())
                .inflation(dto.inflation())
                .unemployment(dto.unemployment())
                .publicDebt(dto.publicDebt())
                .build();
    }
}
