//package com.dayve.jackedlabs.apigateway.filter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//public class RequestLoggingFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        log.info("=".repeat(80));
//        log.info("Incoming Request:");
//        log.info("Path: {}", exchange.getRequest().getPath());
//        log.info("Method: {}", exchange.getRequest().getMethod());
//        log.info("Query Params: {}", exchange.getRequest().getQueryParams());
//
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        log.info("Headers:");
//        headers.forEach((key, value) -> {
//            if (key.equalsIgnoreCase("Authorization")) {
//                log.info("  {}: Bearer [REDACTED]", key);
//            } else {
//                log.info("  {}: {}", key, value);
//            }
//        });
//        log.info("=".repeat(80));
//
//        return chain.filter(exchange)
//                .doFinally(signalType -> {
//                    log.info("Response Status: {}", exchange.getResponse().getStatusCode());
//                    log.info("Signal Type: {}", signalType);
//                });
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//}