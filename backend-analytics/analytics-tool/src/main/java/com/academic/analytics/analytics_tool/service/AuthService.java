package com.academic.analytics.analytics_tool.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class AuthService {

    private static final String GOOGLE_CLIENT_ID = "97031596166-p0s80nr5ptse1sd49galglh2fnu1u8r8.apps.googleusercontent.com";

    public ResponseEntity<?> handleGoogleLogin(String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing Google token");
        }

        GoogleIdToken.Payload payload = verifyGoogleToken(token);
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
        }

        String email = payload.getEmail();
        String role = getUserRole(email);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "name", payload.get("name"),
                "email", email,
                "picture", payload.get("picture"),
                "role", role
        ));
    }

    private GoogleIdToken.Payload verifyGoogleToken(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            return idToken != null ? idToken.getPayload() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getUserRole(String email) {
        if ("aqilahrosidi@gmail.com".equals(email) || email.endsWith("@uptmcounselor.edu.my")) {
            return "COUNSELOR";
        }
        return "STUDENT";
    }
}
