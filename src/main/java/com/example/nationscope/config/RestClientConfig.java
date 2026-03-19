package com.example.nationscope.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient countriesRestClient(){
        return RestClient.builder()
                .baseUrl("https://restcountries.com/v3.1")
                .build();
    }

    @Bean
    public RestClient worldBankRestClient(){
        return RestClient.builder()
                .baseUrl("https://api.worldbank.org/v2")
                .build();
    }
}
