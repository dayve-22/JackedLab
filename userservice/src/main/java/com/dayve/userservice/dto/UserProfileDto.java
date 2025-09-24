package com.dayve.userservice.dto;

import com.dayve.userservice.model.enums.Sex;
import com.dayve.userservice.model.enums.WorkoutGoal;
import com.dayve.userservice.model.enums.WorkoutIntensity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserProfileDto(
        @NotNull @Positive(message = "Height must be positive")
        Double height,

        @NotNull @Positive(message = "Weight must be positive")
        Double weight,

        @NotNull @Min(value = 13, message = "Age must be at least 13")
        Integer age,

        @NotBlank(message = "Sex is required")
        Sex sex,

        @NotBlank(message = "Workout goal is required")
        WorkoutGoal workoutGoal,

        @NotBlank(message = "Workout intensity is required")
        WorkoutIntensity workoutIntensity
) {}
