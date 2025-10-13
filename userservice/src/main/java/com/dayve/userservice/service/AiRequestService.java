package com.dayve.userservice.service;

import com.dayve.userservice.dto.CalorieCalculationPayload;
import com.dayve.userservice.dto.CalorieResponseDto;
import com.dayve.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class AiRequestService {

    private final WebClient aiServiceWebClient;

    public Mono<CalorieResponseDto> getCalorieCalculation(User user) {
        CalorieCalculationPayload payload = new CalorieCalculationPayload(
                user.getAge(),
                user.getHeight(),
                user.getWeight(),
                user.getSex().name(),
                user.getWorkoutIntensity().name(),
                user.getWorkoutGoal().name()
        );

        return aiServiceWebClient.post()
                .uri("/internal/ai/calculate-calories")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(CalorieResponseDto.class);
    }
}