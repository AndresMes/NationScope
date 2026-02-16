package com.example.nationscope.controller;

import com.example.nationscope.service.impl.OrchestratorImpl;
import com.example.nationscope.dto.response.CountryDTOResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/countries")
public class NationScopeController {

    private final OrchestratorImpl orchestor;

    @GetMapping("/{countryName}")
    public ResponseEntity<CountryDTOResponse> getCountryByName(@PathVariable String countryName){
        return ResponseEntity.ok(orchestor.findCountryByName(countryName));
    }
}
