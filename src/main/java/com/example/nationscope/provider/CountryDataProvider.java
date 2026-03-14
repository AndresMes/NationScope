package com.example.nationscope.provider;

import com.example.nationscope.client.CountriesClient;
import com.example.nationscope.client.WorldBankClient;
import com.example.nationscope.dto.external.CountryDtoRestCountries;
import com.example.nationscope.dto.external.WorldBankPointDTO;
import com.example.nationscope.dto.response.EconomicIndicatorsDTOResponse;
import com.example.nationscope.dto.response.SocialIndicatorsDTOResponse;
import com.example.nationscope.service.DevelopmentIndexService;
import com.example.nationscope.utils.CountryCodeConverter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CountryDataProvider {

    private final CountriesClient countriesClient;
    private final WorldBankClient worldBankClient;
    private final DevelopmentIndexService developmentIndexService;
    private final CountryCodeConverter countryCodeConverter;

    @Cacheable(value = "countries_basic", key = "#countryName")
    @CircuitBreaker(name = "restCountriesCB", fallbackMethod = "fallbackRestCountries")
    public CountryDtoRestCountries getCountryMainInformation(String countryName){
        return countriesClient.getCountryByName(countryName.toLowerCase());
    }

    @Cacheable(value = "countries_economics", key = "#countryName")
    @CircuitBreaker(name = "worldBankCB", fallbackMethod = "fallbackEconomics")
    public EconomicIndicatorsDTOResponse buildEconomicIndicators(String countryName) {

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
    @CircuitBreaker(name = "worldBankCB", fallbackMethod = "fallbackSocial")
    public SocialIndicatorsDTOResponse buildSocialIndicators(String countryName){
        return developmentIndexService.calculateDevelopmentIndexes(countryName);
    }

    private CountryDtoRestCountries fallbackRestCountries(String countryName, Throwable e) {
        return new CountryDtoRestCountries(new CountryDtoRestCountries.NameDTO(""), List.of(), List.of(), Double.valueOf(0d), 0l, List.of(), Map.of(), Map.of());
    }

    private EconomicIndicatorsDTOResponse fallbackEconomics(String countryName, Throwable e){
        return EconomicIndicatorsDTOResponse.builder()
                .gdp(new BigDecimal("0.0"))
                .inflation(0d)
                .growthRate(0d)
                .publicDebt(0d)
                .unemployment(0d)
                .build();
    }

    private SocialIndicatorsDTOResponse fallbackSocial(String countryName, Throwable e){
        return SocialIndicatorsDTOResponse.builder()
                .hdi(0d)
                .educationIndex(0d)
                .lifeExpectancy(0d)
                .literacyRate(0d)
                .povertyRate(0d)
                .build();
    }

    private Double safeGetDouble(WorldBankPointDTO dto) {
        return (dto != null && dto.value() != null) ? dto.value().doubleValue() : null;
    }

    private BigDecimal safeGetBigDecimal(WorldBankPointDTO dto) {
        return (dto != null && dto.value() != null) ? dto.value() : null;
    }
}
