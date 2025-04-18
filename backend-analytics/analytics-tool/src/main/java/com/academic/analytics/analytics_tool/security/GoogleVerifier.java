package com.academic.analytics.analytics_tool.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GoogleVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleVerifier() {
        try {
            // Initialize GoogleIdTokenVerifier with proper transport and JSON factory
            this.verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance() // Using GsonFactory
                )
                .setAudience(getClientIds()) // Add multiple client IDs if necessary
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Google verifier: " + e.getMessage(), e);
        }
    }

    /**
     * Verifies the provided Google token and returns the payload if valid.
     *
     * @param token Google token to verify.
     * @return Payload of the Google token if valid; null otherwise.
     */
    public GoogleIdToken.Payload verifyToken(String token) {
        System.out.println("Verifying Google token: " + token); // Debugging
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                System.out.println("Google token verified successfully!");
                return idToken.getPayload();
            } else {
                System.err.println("Invalid Google token.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Google token verification failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a list of client IDs that are allowed for verification.
     *
     * @return List of allowed client IDs.
     */
    private List<String> getClientIds() {
        return Collections.singletonList("681268107396-qvlmu3mlgsns92rle86mln83ng2fq9l6.apps.googleusercontent.com"); // Add more if needed
    }
}