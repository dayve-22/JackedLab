package com.dayve.userservice.controller;


import com.dayve.userservice.dto.CalorieResponseDto;
import com.dayve.userservice.model.User;
import com.dayve.userservice.repository.UserRepository;
import com.dayve.userservice.service.AiRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public Mono<CalorieResponseDto> calculateCalories(
            @RequestHeader("X-User-ID") String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found for keycloakId: " + keycloakId));
        return aiRequestService.getCalorieCalculation(user);
    }
}
