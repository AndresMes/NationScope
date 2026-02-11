package com.example.nationscope.domain;

import java.util.List;
import java.util.TimeZone;

public class Country {

    String name;
    String capital;
    List<String> continents;
    Double area;
    Long population;
    List<TimeZone> timeZones;
    List<String> languages;

    EconomicIndicators economicIndicators;
    SocialIndicators socialIndicators;

}
