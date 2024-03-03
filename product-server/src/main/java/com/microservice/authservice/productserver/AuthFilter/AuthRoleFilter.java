package com.microservice.authservice.productserver.AuthFilter;


import com.microservice.authservice.productserver.Service.ProductService.JWT.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Component
@Slf4j
public class AuthRoleFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    public AuthRoleFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("intercepted by custom filter ");

        if (request.getRequestURI().startsWith("api/v1/product/actuator") ||
                request.getRequestURI().startsWith("api/v1/product/eureka") ||
                request.getRequestURI().startsWith("api/v1/product/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token;
        try {
            token = jwtService.getJWTFromRequest(request);

            log.info("Jwt Token {}",token);

            if (!jwtService.validateToken(token)) {
                throw new AccessDeniedException("Token is invalid Product - service");
            }

            if (Objects.isNull(token)) {
                log.info("Objects.isNull .");
                throw new RuntimeException("Unauthorized request");
            }
            String role = jwtService.getRoleFromToken(token);

            if (request.getRequestURI().startsWith("/api/v1/admin/")) {
                if (role.equals("USER")) {
                    log.info("Invalid URI with invalid ROLE {} : {}", request.getRequestURI(), role);
                    throw new AccessDeniedException("Invalid URI with invalid ROLE {} : {}", request.getRequestURI(), role);
                }
            }
        } catch (ExpiredJwtException e) {
            log.error("Expired jwt");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Expired Jwt");
            return;
        } catch (InvalidClaimException e) {
            log.error("Invalid Claims");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tampered Jwt token");
            return;
        }catch (AccessDeniedException e){
            log.error("Access Denied ");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid URI with invalid ROLE");
            return;
        } catch (Exception e) {
            log.error("Something went wrong while validating the token {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Something went wrong while parsing the jwt token");
            return;
        }finally {
            log.info("Product Service Filtering Completed.....");
        }
        request.setAttribute("userId", jwtService.getUserIdFromToken(token));
        filterChain.doFilter(request, response);
    }
}
