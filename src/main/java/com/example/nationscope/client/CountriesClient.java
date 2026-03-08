package com.example.nationscope.client;

import com.example.nationscope.dto.external.CountryDtoRestCountries;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CountriesClient {

    private final RestClient countriesClient;

    public CountriesClient(@Qualifier("countriesRestClient") RestClient clientCountries) {
        this.countriesClient = clientCountries;
    }

    public CountryDtoRestCountries getCountryByName(String countryName){

        try{
            CountryDtoRestCountries[] response = countriesClient.get()
                    .uri("/name/" + countryName)
                    .retrieve()
                    .body(CountryDtoRestCountries[].class);

            return (response != null &&  response.length > 0) ? response[0] : null;

        }catch (HttpClientErrorException.NotFound ex){
            throw new IllegalArgumentException("Country not found or mistyped");
        }

    }
}
