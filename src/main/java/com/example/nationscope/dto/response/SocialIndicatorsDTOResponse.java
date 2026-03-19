package com.example.nationscope.dto.response;

import lombok.Builder;

@Builder
public record SocialIndicatorsDTOResponse(
        Double literacyRate,
        Double lifeExpectancy,
        Double hdi,
        Double povertyRate,
        Double educationIndex
) {
}
