package com.example.nationscope.service.impl;

import com.example.nationscope.client.CountriesClient;
import com.example.nationscope.domain.EconomicIndicators;
import com.example.nationscope.domain.SocialIndicators;
import com.example.nationscope.dto.external.CountryDtoRestCountries;
import com.example.nationscope.dto.response.CountryDTOResponse;
import com.example.nationscope.service.AgreggationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreggationServiceImpl implements AgreggationService {

    private final CountriesClient countriesClient;

    @Override
    public CountryDTOResponse buildCountryEntity(String countryName) {

        if(countryName == null){
            throw new IllegalArgumentException("Argument cannot be null");
        }

        String countryNameLowered = countryName.toLowerCase();

        CountryDtoRestCountries response = countriesClient.getCountryByName(countryNameLowered);

        return CountryDTOResponse.builder()
                .name(response.name().common())
                .capital(response.capital() != null
                        ? response.capital()
                        : List.of())
                .area(response.area())
                .languages(response.languages())
                .population(response.population())
                .continents(response.continents())
                .timeZones(response.timeZones())

                //temporales mientras se implementa el consumo de la API correspondiente
                .socialIndicators(new SocialIndicators())
                .economicIndicators(new EconomicIndicators())
                .build();

    }
}
