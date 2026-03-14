package com.example.nationscope.service.impl;

import com.example.nationscope.client.CountriesClient;
import com.example.nationscope.client.WorldBankClient;
import com.example.nationscope.dto.external.CountryDtoRestCountries;
import com.example.nationscope.dto.external.WorldBankPointDTO;
import com.example.nationscope.dto.response.CountryDTOResponse;
import com.example.nationscope.dto.response.EconomicIndicatorsDTOResponse;
import com.example.nationscope.dto.response.SocialIndicatorsDTOResponse;
import com.example.nationscope.provider.CountryDataProvider;
import com.example.nationscope.service.AgreggationService;
import com.example.nationscope.service.DevelopmentIndexService;
import com.example.nationscope.utils.CountryCodeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreggationServiceImpl implements AgreggationService {

    private final CountryDataProvider countryDataProvider;

    @Override
    public CountryDTOResponse buildCountryEntity(String countryName) {
        if (countryName == null || countryName.isBlank()) {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }

        CountryDtoRestCountries restCountriesResponse = countryDataProvider.getCountryMainInformation(countryName);

        EconomicIndicatorsDTOResponse ecoDto = countryDataProvider.buildEconomicIndicators(countryName);

        SocialIndicatorsDTOResponse socialIndicators = countryDataProvider.buildSocialIndicators(countryName);


        return CountryDTOResponse.builder()
                .name(restCountriesResponse.name().common())
                .capital(restCountriesResponse.capital() != null ? restCountriesResponse.capital() : List.of())
                .timeZones(restCountriesResponse.timeZones())
                .continents(restCountriesResponse.continents())
                .area(restCountriesResponse.area())
                .population(restCountriesResponse.population())
                .currencies(restCountriesResponse.currencies())
                .languages(restCountriesResponse.languages())
                .economicIndicators(ecoDto)
                .socialIndicators(socialIndicators)
                .build();
    }

}
