package com.microservice.apigateway.apigateway.Filter.Util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class Validator {

    private final String jwtSecret = "ZmFrbKapka2Y7YWprZGZqYTtsZGZrYW";

    public static final List<String> openApiEndPoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/eureka"
    );
    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openApiEndPoints.stream().noneMatch(url ->
                    serverHttpRequest.getURI().getPath().contains(url));


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Jwt token is expired or is invalid");
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}