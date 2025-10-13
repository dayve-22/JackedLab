package com.dayve.aiservice.dto;

public record CalorieCalculationPayload(
        int age,
        double heightCm,
        double weightKg,
        String gender,
        String activityLevel,
        String goal
) {}
