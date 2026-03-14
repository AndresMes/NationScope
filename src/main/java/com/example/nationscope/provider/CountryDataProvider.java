package com.example.nationscope.provider;

import com.example.nationscope.client.CountriesClient;
import com.example.nationscope.client.WorldBankClient;
import com.example.nationscope.dto.external.CountryDtoRestCountries;
import com.example.nationscope.dto.external.WorldBankPointDTO;
import com.example.nationscope.dto.response.EconomicIndicatorsDTOResponse;
import com.example.nationscope.dto.response.SocialIndicatorsDTOResponse;
import com.example.nationscope.service.DevelopmentIndexService;
import com.example.nationscope.utils.CountryCodeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CountryDataProvider {

    private final CountriesClient countriesClient;
    private final WorldBankClient worldBankClient;
    private final DevelopmentIndexService developmentIndexService;
    private final CountryCodeConverter countryCodeConverter;

    @Cacheable(value = "countries_basic", key = "#countryName")
    public CountryDtoRestCountries getCountryMainInformation(String countryName){

        System.out.println("!!! BUSCANDO EN LA API EXTERNA (NO EN CACHÉ) para: " + countryName + " EN MAIN INFO");
        return countriesClient.getCountryByName(countryName.toLowerCase());
    }

    @Cacheable(value = "countries_economics", key = "#countryName")
    public EconomicIndicatorsDTOResponse buildEconomicIndicators(String countryName) {

        System.out.println("!!! BUSCANDO EN LA API EXTERNA (NO EN CACHÉ) para: " + countryName + " EN ECONOMIC");
        String isoCode = countryCodeConverter.convertToIso(countryName);

        return EconomicIndicatorsDTOResponse.builder()
                .gdp(safeGetBigDecimal(worldBankClient.getGdpByCountry(isoCode)))
                .growthRate(safeGetDouble(worldBankClient.getGrowthRateByCountry(isoCode)))
                .inflation(safeGetDouble(worldBankClient.getInflationByCountry(isoCode)))
                .unemployment(safeGetDouble(worldBankClient.getUnemploymentIndicatorByCountry(isoCode)))
                .publicDebt(safeGetDouble(worldBankClient.getPublicDebtByCountry(isoCode)))
                .build();
    }

    @Cacheable(value = "countries_socials", key = "#countryName")
    public SocialIndicatorsDTOResponse buildSocialIndicators(String countryName){
        System.out.println("!!! BUSCANDO EN LA API EXTERNA (NO EN CACHÉ) para: " + countryName + " EN SOCIAL");
        return developmentIndexService.calculateDevelopmentIndexes(countryName);
    }

    private Double safeGetDouble(WorldBankPointDTO dto) {
        return (dto != null && dto.value() != null) ? dto.value().doubleValue() : null;
    }

    private BigDecimal safeGetBigDecimal(WorldBankPointDTO dto) {
        return (dto != null && dto.value() != null) ? dto.value() : null;
    }
}
