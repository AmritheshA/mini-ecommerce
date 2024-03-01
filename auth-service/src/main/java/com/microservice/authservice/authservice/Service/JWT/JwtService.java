package com.microservice.authservice.authservice.Service.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class JwtService {

    private final String jwtSecret = "ZmFrbKapka2Y7YWprZGZqYTtsZGZrYW";

    public String generateToken(Authentication authentication, String email) {
        Date currentDate = new Date();
        Date expirationTime = new Date(currentDate.getTime() + 30 * 24 * 60 * 60 * 1000L);
        return Jwts.builder().setSubject(email).claim("userName", authentication.getName()).setExpiration(expirationTime).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt token is expired or is invalid");
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return " ";
    }

}