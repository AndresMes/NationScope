package com.example.nationscope.service;

import com.example.nationscope.dto.response.CountryDTOResponse;

public interface AgreggationService {

    CountryDTOResponse buildCountryEntity(String countryName);
}
