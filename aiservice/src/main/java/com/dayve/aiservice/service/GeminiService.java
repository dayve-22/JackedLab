package com.dayve.aiservice.service;


import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.ThinkingConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiService {

    private final WebClient webClient;

    public GeminiService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    public String getRecommendationFromAi(String prompt) {

        GenerateContentResponse response = new Client().models.generateContent(
                "gemini-2.5-flash",
                prompt,
                GenerateContentConfig.builder()
                        .thinkingConfig(ThinkingConfig.builder().thinkingBudget(0).build())
                        .build()
        );
        return response.text();
    }
}

