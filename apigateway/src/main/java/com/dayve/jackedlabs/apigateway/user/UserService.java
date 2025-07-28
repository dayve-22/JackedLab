package com.dayve.jackedlabs.apigateway.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final WebClient userServiceWebCLient;
    public Mono<Boolean> validateUser(String userId) {
        log.info("Validating for userId: {}",userId);

            return userServiceWebCLient.get()
                    .uri("api/users/{userId}/validate", userId)
                    .retrieve().bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class,e->
            {
                if(e.getStatusCode() ==HttpStatus.NOT_FOUND)
                    return Mono.error(new RuntimeException("User not found: "+userId));
                else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                    return Mono.error(new RuntimeException("Invalid Request: "+userId));
                return Mono.error(new RuntimeException("Unexpected error "+ e.getMessage()));
            });
    }
}
