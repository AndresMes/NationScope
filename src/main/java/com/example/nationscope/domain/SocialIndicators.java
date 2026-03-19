package com.example.nationscope.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocialIndicators {

    private Double literacyRate;
    private Double lifeExpectancy;
    private Double hdi;
    private Double povertyRate;
    private Double educationIndex;
}
