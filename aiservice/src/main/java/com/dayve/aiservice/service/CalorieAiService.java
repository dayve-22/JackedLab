package com.dayve.aiservice.service;

import com.dayve.aiservice.dto.CalorieCalculationPayload;
import com.dayve.aiservice.dto.CalorieResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalorieAiService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public CalorieResponseDto calculateCalories(CalorieCalculationPayload payload) {
        String prompt = createPrompt(payload);
        String aiJsonString = geminiService.getRecommendationFromAi(prompt);

        try {
            return objectMapper.readValue(aiJsonString, CalorieResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    private String createPrompt(CalorieCalculationPayload p) {
        return String.format(
                "Based on the following user data, calculate their daily calorie needs. " +
                        "Data: Age=%d, Height=%.1f cm, Weight=%.1f kg, Gender=%s, Activity Level=%s, Goal=%s. " +
                        "Respond ONLY with a valid JSON object with three keys: " +
                        "'maintenanceCalories' (integer), 'goalCalories' (integer), and 'explanation' (string).",
                p.age(), p.heightCm(), p.weightKg(), p.gender(), p.activityLevel(), p.goal()
        );
    }
}
