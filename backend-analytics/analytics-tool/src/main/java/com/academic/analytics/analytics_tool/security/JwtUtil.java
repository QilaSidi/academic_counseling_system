package com.academic.analytics.analytics_tool.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    // Define a static signing key for JWT
    private static final SecretKey SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token without "ROLE_" prefix.
     *
     * @param email The user's email to be included in the token payload.
     * @param role The user's role (e.g., STUDENT or COUNSELOR) to be included in the token payload.
     * @return A signed JWT token as a String.
     */
    public String generateJwtToken(String email, String role) {
        //  Remove ROLE_ prefixing - The frontend expects the role directly
        /*  if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        } */

        return Jwts.builder()
            .setSubject(email)
            .claim("role", role)  // Use the role as provided, without modification
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h expiration
            .signWith(SIGNING_KEY)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(SIGNING_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(SIGNING_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get("role", String.class);
    }

    public SecretKey getSigningKey() {
        return SIGNING_KEY;
    }
}