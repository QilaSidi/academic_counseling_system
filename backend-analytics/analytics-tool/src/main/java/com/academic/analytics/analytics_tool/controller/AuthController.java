package com.academic.analytics.analytics_tool.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.academic.analytics.analytics_tool.security.JwtUtil;
import com.academic.analytics.analytics_tool.security.GoogleVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private GoogleVerifier googleVerifier;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> request) {
        String googleToken = request.get("token");

        try {
            GoogleIdToken.Payload payload = googleVerifier.verifyToken(googleToken);
            if (payload == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(Map.of("error", "Invalid Google token."));
            }

            String email = payload.getEmail();
            System.out.println("Raw email: '" + email + "'");
            String sanitizedEmail = email.trim().replaceAll("\\p{C}", "");
            System.out.println("Sanitized email: '" + sanitizedEmail + "'");

            String[] emailParts = sanitizedEmail.split("@");
            if (emailParts.length != 2 || !emailParts[1].equalsIgnoreCase("student.kuptm.edu.my")) {
                System.out.println("Rejected email domain: " + sanitizedEmail);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .body(Map.of(
                                         "error", "Access denied. Please use your KUPTM student email.",
                                         "details", "Allowed domain: @student.kuptm.edu.my"
                                     ));
            }

            String role = "STUDENT";
            String jwt = jwtUtil.generateJwtToken(sanitizedEmail, role);

            Map<String, String> response = new HashMap<>();
            response.put("jwt", jwt);
            response.put("role", role);
            response.put("email", sanitizedEmail);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "Token verification failed.", "details", e.getMessage()));
        }
    }
}
