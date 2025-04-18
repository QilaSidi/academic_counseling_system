
package com.academic.analytics.analytics_tool.controller;

import com.academic.analytics.analytics_tool.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentAuthController {

    private final JwtUtil jwtUtil;

    public StudentAuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || !email.trim().toLowerCase().endsWith("@students.kuptm.edu.my")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Only student emails are allowed.",
                "details", "Please use your official KUPTM student email."
            ));
        }

        if (password == null || !"password123".equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "error", "Invalid credentials.",
                "details", "Ensure your email and password are correct."
            ));
        }

        // Corrected: Use "STUDENT" directly and store the result in "token"
        String token = jwtUtil.generateJwtToken(email, "STUDENT"); 

        // Corrected: Return the "token" variable
        return ResponseEntity.ok(Map.of(
            "jwt", token,
            "role", "STUDENT",
            "email", email.trim()
        ));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getSigningKey())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            System.out.println("JWT Role: " + role);
            System.out.println("JWT Email: " + email);

            if (!"STUDENT".equalsIgnoreCase(role)) {  // Corrected: Expect "STUDENT" without prefix
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: role is not STUDENT.");
            }

            return ResponseEntity.ok(Map.of(
                "message", "Welcome to the Student Dashboard!",
                "email", email,
                "role", role
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }
}