package com.example.nationscope.service;

import com.example.nationscope.dto.response.SocialIndicatorsDTOResponse;

public interface DevelopmentIndexService {

    SocialIndicatorsDTOResponse calculateDevelopmentIndexes(String country);
}
