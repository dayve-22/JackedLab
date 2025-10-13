package com.dayve.aiservice.controller;

import com.dayve.aiservice.dto.CalorieCalculationPayload;
import com.dayve.aiservice.dto.CalorieResponseDto;
import com.dayve.aiservice.service.CalorieAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/ai")
@RequiredArgsConstructor
public class UserAnalysisController {

    private final CalorieAiService calorieAiService;

    @PostMapping("/calculate-calories")
    public CalorieResponseDto calculateCalories(@RequestBody CalorieCalculationPayload payload) {
        return calorieAiService.calculateCalories(payload);
    }
}
