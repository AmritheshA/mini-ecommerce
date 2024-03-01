package com.microservice.apigateway.apigateway.Filter.GatewayFilter;

import com.microservice.apigateway.apigateway.Filter.Util.Validator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    private final Validator routeValidator;


    public AuthorizationFilter(Validator routeValidator) {
        super(Config.class);
        this.routeValidator = routeValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("Filter Applied....");
            ServerHttpRequest request = exchange.getRequest();

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
                String token;
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    try {
                        log.info("validation token .... ");
                        if (routeValidator.validateToken(token)) {
                            request = exchange
                                    .getRequest()
                                    .mutate()
                                    .header("token", token)
                                    .header("username", routeValidator.getUsernameFromToken(token))
                                    .build();
                        } else {
                            log.error("Invalid token....");
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    } catch (Exception e) {
                        log.error("Something went wrong in authorization filter");
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    @Getter
    @Service
    public static class Config {
        private List<String> allowedRoles;
    }
}