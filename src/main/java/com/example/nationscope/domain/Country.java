package com.example.nationscope.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Country {

    String name;
    List<String> capital;
    List<String> continents;
    Double area;
    Long population;
    List<String> timeZones;
    List<String> languages;

    EconomicIndicators economicIndicators;
    SocialIndicators socialIndicators;

}
