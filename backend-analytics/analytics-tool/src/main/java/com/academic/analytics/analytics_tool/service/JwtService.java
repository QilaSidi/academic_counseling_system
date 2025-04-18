package com.academic.analytics.analytics_tool.service;
import com.academic.analytics.analytics_tool.model.Counselor;  // Add this import

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String SECRET_KEY = "your-secret-key"; // Use a strong secret key
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    // Generate JWT token for counselor
    public String generateToken(Counselor counselor) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // Create a SecretKey using the SECRET_KEY (as a byte array) and the algorithm
        Key secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        // Generate the JWT token
        return Jwts.builder()
                .setSubject(counselor.getEmail())  // Subject could be the counselor's email (or user identifier)
                .claim("role", counselor.getRole()) // You can add more claims as needed, like user roles
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)  // Using the SecretKey object
                .compact();
    }

    // Validate JWT token and extract claims
    public Claims validateToken(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())  // Use a byte array for the key
                    .build();

            return jwtParser.parseClaimsJws(token).getBody();  // Parse and get the body (claims)
        } catch (Exception e) {
            // If the token is invalid or expired, return null
            return null;
        }
    }
}
