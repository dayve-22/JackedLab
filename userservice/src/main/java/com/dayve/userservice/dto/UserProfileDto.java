package com.dayve.userservice.dto;

import com.dayve.userservice.model.enums.Sex;
import com.dayve.userservice.model.enums.WorkoutGoal;
import com.dayve.userservice.model.enums.WorkoutIntensity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserProfileDto(
        @NotBlank(message = "Keycloak ID is required")
        String keycloakId,

        @NotNull(message = "Weight is required")
        Double weight,

        @NotNull(message = "Height is required")
        Double height,

        @NotNull(message = "Age is required")
        @Positive(message = "Age must be greater than 0")
        Integer age,

        @NotNull(message = "Sex is required")
        Sex sex,

        @NotNull(message = "Workout goal is required")
        WorkoutGoal workoutGoal,

        @NotNull(message = "Workout intensity is required")
        WorkoutIntensity workoutIntensity
) {}
