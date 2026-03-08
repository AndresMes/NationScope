package com.example.nationscope.config;

import com.google.genai.Client;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    public Client geminiClient(){
        return new Client();
    }
}
