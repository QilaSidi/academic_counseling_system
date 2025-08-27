package com.example.newappointmentsystem.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map; // Used for decoding the secret
import java.util.function.Function; // Used for key generation

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims; // NEW IMPORT: Use SecretKey instead of java.security.Key
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretString;

    private SecretKey signingKey;

    

    @PostConstruct
    public void init() {
        // This is the correct way to generate the SecretKey for jjwt 0.11.x / 0.12.x
        // assuming your 'secret' in application.properties is a Base64 encoded string.
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));

        // If your secret is *not* Base64 encoded and is just a plain string, you'd use:
        // this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // THIS IS THE CORRECT SYNTAX for JJWT 0.11.x / 0.12.x
        return Jwts.parser() // Correct for 0.12.x after .parserBuilder().build() is inferred.
                   .verifyWith(signingKey) // CHANGE: For 0.12.x, use verifyWith(SecretKey)
                   .build() // ADDED: Ensure .build() is called after setting parser properties
                   .parseSignedClaims(token) // CHANGE: For 0.12.x, use parseSignedClaims
                   .getPayload(); // CHANGE: For 0.12.x, use getPayload() instead of getBody()
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        // This is the correct syntax for jjwt 0.11.x / 0.12.x
        return Jwts.builder()
                .claims(claims) // CHANGE: For 0.12.x, use .claims() instead of .setClaims()
                .subject(subject) // CHANGE: For 0.12.x, use .subject() instead of .setSubject()
                .issuedAt(new Date(System.currentTimeMillis())) // CHANGE: For 0.12.x, use .issuedAt()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours // CHANGE: For 0.12.x, use .expiration()
                .signWith(signingKey, Jwts.SIG.HS256) // Correct syntax for 0.12.x
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(username) && !isTokenExpired(token));
    }
}