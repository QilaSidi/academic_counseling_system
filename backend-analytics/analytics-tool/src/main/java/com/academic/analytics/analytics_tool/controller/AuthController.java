package com.academic.analytics.analytics_tool.controller;
// this is auth controller for the backend testing only
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academic.analytics.analytics_tool.security.JwtUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    @Value("${app.studentDomain}")
    private String studentDomain;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        if (email == null || !email.contains("@")) {
            return ResponseEntity.badRequest().body("Invalid email");
        }

        String role = email.endsWith(studentDomain) ? "student" : "counselor";

        String token = jwtUtil.generateToken(email, role);

        return ResponseEntity.ok(Map.of("token", token, "role", role));
    }
}

