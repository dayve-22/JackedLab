package com.dayve.userservice.dto;

public record CalorieResponseDto(
        int maintenanceCalories,
        int goalCalories,
        String explanation
) {}
