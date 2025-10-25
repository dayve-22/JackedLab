package com.dayve.jackedlabs.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test/auth")
    public Mono<Map<String, Object>> testAuth(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        log.info("TEST ENDPOINT HIT!");
        log.info("Authorization Header Present: {}", authHeader != null);

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("authenticated", authentication.isAuthenticated());
                    response.put("principal", authentication.getName());
                    response.put("authorities", authentication.getAuthorities());

                    if (authentication.getPrincipal() instanceof Jwt jwt) {
                        response.put("sub", jwt.getSubject());
                        response.put("email", jwt.getClaim("email"));
                        response.put("issuer", jwt.getIssuer());
                    }

                    log.info("Authentication successful: {}", authentication.getName());
                    return response;
                })
                .defaultIfEmpty(Map.of("authenticated", false, "message", "No authentication found"))
                .doOnError(error -> log.error("Error in test endpoint", error));
    }

    @GetMapping("/test/public")
    public Mono<Map<String, Object>> testPublic() {
        log.info("PUBLIC TEST ENDPOINT HIT!");
        return Mono.just(Map.of("message", "This is public", "timestamp", System.currentTimeMillis()));
    }
}