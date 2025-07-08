package com.dayve.activityservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {
    private final WebClient userServiceWebCLient;
    public boolean validateUser(String userId) {
        log.info("Validating for userId: {}",userId);
        try {
            return userServiceWebCLient.get()
                    .uri("api/users/{userId}/validate", userId)
                    .retrieve().bodyToMono(Boolean.class)
                    .block();

        } catch (WebClientResponseException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new RuntimeException("User not found with id "+ userId);
            }
            else{
                throw new RuntimeException("Invalid request");
            }
        }
    }
}
