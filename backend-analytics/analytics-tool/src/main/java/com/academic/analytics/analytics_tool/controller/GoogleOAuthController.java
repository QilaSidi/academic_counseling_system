package com.academic.analytics.analytics_tool.controller;
// this auth controller for prooduction test on;y (by through frontend)
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.http.ResponseEntity; // Import HashMap
import org.springframework.web.bind.annotation.PostMapping;   // Import Map
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academic.analytics.analytics_tool.security.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@RestController
@RequestMapping("/api/auth")
public class GoogleOAuthController {

    private final JwtUtil jwtUtil;
    private final GoogleIdTokenVerifier verifier;

    private final String STUDENT_CLIENT_ID = "681268107396-qvlmu3mlgsns92rle86mln83ng2fq9l6.apps.googleusercontent.com";
    private final String COUNSELOR_CLIENT_ID = "97031596166-p0s80nr5ptse1sd49galglh2fnu1u8r8.apps.googleusercontent.com";

    public GoogleOAuthController(JwtUtil jwtUtil) throws Exception {
        this.jwtUtil = jwtUtil;

        this.verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(
                        Collections.unmodifiableList(
                                java.util.List.of(STUDENT_CLIENT_ID, COUNSELOR_CLIENT_ID)
                        ))
                .build();
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody TokenRequest tokenRequest) {
        String idTokenString = tokenRequest.getIdToken();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String role;

                if (email.endsWith("student.kuptm.edu.my")) {
                    role = "student";
                } else if (email.endsWith("kuptm.edu.my")) {
                    role = "counselor";
                } else {
                    role = "counselor"; // For testing
                }

                // Generate JWT with email and role embedded
                String jwt = jwtUtil.generateToken(email, role);

                // --- MODIFICATION START ---
                // You can either modify JwtResponse to include email
                // OR simply return a Map directly, which is often simpler for custom responses

                // Option 1: Modify JwtResponse (recommended for strong typing)
                return ResponseEntity.ok(new JwtResponse(jwt, role, email)); // Pass email here

                // Option 2: Return a Map (alternative if you don't want to change DTO)
                // Map<String, String> responseBody = new HashMap<>();
                // responseBody.put("token", jwt);
                // responseBody.put("role", role);
                // responseBody.put("email", email); // Add email to the map
                // return ResponseEntity.ok(responseBody);
                // --- MODIFICATION END ---

            } else {
                return ResponseEntity.status(401).body("Invalid ID token");
            }
        } catch (GeneralSecurityException | java.io.IOException e) {
            return ResponseEntity.status(500).body("Token verification failed: " + e.getMessage());
        }
    }

    public static class TokenRequest {
        private String idToken;

        public String getIdToken() { return idToken; }
        public void setIdToken(String idToken) { this.idToken = idToken; }
    }

    // MODIFIED JwtResponse DTO
    public static class JwtResponse {
        private final String token;
        private final String role;
        private final String email; // Add the email field

        public JwtResponse(String token, String role, String email) { // Add email to constructor
            this.token = token;
            this.role = role;
            this.email = email; // Initialize email
        }

        public String getToken() { return token; }
        public String getRole() { return role; }
        public String getEmail() { return email; } // Add getter for email
    }
}