package com.academic.analytics.analytics_tool.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function; // Import for secure key generation

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component; // New import for @PostConstruct

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts; // New import for Base64 decoding
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private Key key; // Changed from SECRET_KEY to key

    // You can set this in application.properties or .yaml
    @Value("${jwt.secret}")
    private String secretKeyBase64; // Renamed to reflect Base64 encoding

      // --- CHANGE STARTS HERE ---
    // Inject the expiration time from application.properties
    @Value("${jwt.expiration.milliseconds}") // This line tells Spring to inject the value
    private long jwtExpirationInMillis;      // Remove 'final' if you want it to be configurable,
                                             // or keep 'final' if you're sure it's always set via @Value
    // --- CHANGE ENDS HERE ---

    @PostConstruct // New method for initialization after dependency injection
    public void init() {
        // Decode the Base64 secret key from config and generate the Key object
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    // --- Public methods to extract claims ---

    // This method is now redundant as extractEmail directly gets the subject
    // public String extractUsername(String token) {
    //     return extractClaim(token, Claims::getSubject);
    // }

    public String extractEmail(String token) throws Exception {
        return validateTokenAndGetClaims(token).getSubject();
    }

    public String extractRole(String token) throws Exception {
        // Assuming the role is stored as a custom claim named "role"
        return (String) validateTokenAndGetClaims(token).get("role");
    }

    public Date extractExpiration(String token) throws Exception {
        return validateTokenAndGetClaims(token).getExpiration();
    }

    // Simplified extractClaim, now uses validateTokenAndGetClaims internally
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = validateTokenAndGetClaims(token);
        return claimsResolver.apply(claims);
    }

    // New method to validate token and get claims
    public Claims validateTokenAndGetClaims(String token) throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key) // Use the initialized 'key'
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Catch specific JWT exceptions for better error handling in a real app
            throw new Exception("Invalid or expired JWT token: " + e.getMessage());
        }
    }

    // --- Token Generation ---

    public String generateToken(String email, String role) { // Changed username to email
        return Jwts.builder()
                .setSubject(email) // Set email as subject
                .claim("role", role) // Add role as a custom claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis)) // Use fixed expiration
                .signWith(key, SignatureAlgorithm.HS256) // Use the initialized 'key'
                .compact();
    }

    // Removed private createToken method as generateToken is now direct
    // Removed private extractAllClaims method as validateTokenAndGetClaims covers it
    // Removed private isTokenExpired method as validateTokenAndGetClaims covers it

    // Removed validateToken method as validateTokenAndGetClaims covers its core functionality
    // public Boolean validateToken(String token, String expectedUsername) {
    //     final String username = extractUsername(token);
    //     return (username.equals(expectedUsername) && !isTokenExpired(token));
    // }
}
