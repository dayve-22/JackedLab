package com.dayve.jackedlabs.apigateway;


import com.dayve.jackedlabs.apigateway.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {
    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(userId != null && token != null){
            return userService.validateUser(userId)
                    .flatMap(exist->{
                        if(!exist){
                            //Register user
                        }
                        else{
                            log.info("User already exist, Skipping sync.");
                            return Mono.empty();
                        }
                    }).then(Mono.defer(()->{
                        ServerHttpRequest muy
                    })
        }
    }
}
