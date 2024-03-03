package com.microservice.apigateway.apigateway.RouteConfig;

import com.microservice.apigateway.apigateway.Filter.GatewayFilter.AuthorizationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private final AuthorizationFilter authorizationFilter;

    public RouteConfig(AuthorizationFilter authorizationFilter) {
        this.authorizationFilter = authorizationFilter;
    }

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(path ->
                        path.path("/api/v1/auth/**")
                                .filters(f -> f.filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                                .uri("lb://auth-service"))
                .route(path ->
                        path.path("/api/v1/product/**")
                                .filters(f -> f.filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                                .uri("lb://product-service"))
                .route(path ->
                        path.path("/api/v1/admin/product/**")
                                .filters(f -> f.filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                                .uri("lb://product-service"))
                .route(path ->
                        path.path("/api/v1/cart/**")
                                .filters(f -> f.filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                                .uri("lb://cart-service"))
                .build();
    }

}
