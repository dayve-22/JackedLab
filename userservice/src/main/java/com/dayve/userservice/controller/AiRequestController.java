package com.dayve.userservice.controller;


import com.dayve.userservice.dto.CalorieResponseDto;
import com.dayve.userservice.model.User;
import com.dayve.userservice.repository.UserRepository;
import com.dayve.userservice.service.AiRequestService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ai")
public class AiRequestController {

    private final AiRequestService aiRequestService;
    private final UserRepository userRepository;

    @Autowired
    public AiRequestController(AiRequestService aiRequestService,UserRepository userRepository){
        this.aiRequestService=aiRequestService;
        this.userRepository=userRepository;
    }

    @PostMapping("/calculate-calories")
    public Mono<CalorieResponseDto> calculateCalories(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return aiRequestService.getCalorieCalculation(user);
    }
}
