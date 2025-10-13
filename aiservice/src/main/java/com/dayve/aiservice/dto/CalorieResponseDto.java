package com.dayve.aiservice.dto;

public record CalorieResponseDto(
        int maintenanceCalories,
        int goalCalories,
        String explanation
) {}
