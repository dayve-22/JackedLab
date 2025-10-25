package com.dayve.userservice.dto;

public record CalorieCalculationPayload(
        Integer age,
        Double heightCm,
        Double weightKg,
        String gender,
        String activityLevel,
        String goal
) {}
