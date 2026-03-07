package com.example.nationscope.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class CountryCodeConverter {

    private final Map<String, String> countryMap = new HashMap<>();

    @PostConstruct
    public void init() {
        String[] isoCountries = Locale.getISOCountries();
        Locale english = new Locale("en");

        for (String code : isoCountries) {
            Locale locale = new Locale("", code);
            String name = locale.getDisplayCountry(english).toLowerCase();
            countryMap.put(name, code);
        }
    }

    public String convertToIso(String countryName) {
        if (countryName == null) return null;
        return countryMap.get(countryName.trim().toLowerCase());
    }

}
