package com.academic.analytics.analytics_tool.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.academic.analytics.analytics_tool.dto.AuthRequest;
import com.academic.analytics.analytics_tool.dto.AuthResponse;
import com.academic.analytics.analytics_tool.dto.GoogleLoginRequest;
import com.academic.analytics.analytics_tool.dto.RegisterRequest;
import com.academic.analytics.analytics_tool.model.Counselor;
import com.academic.analytics.analytics_tool.repository.CounselorRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;


@Service
public class AuthService {

    @Autowired
    private CounselorRepository counselorRepository;

    @Autowired
    private JwtService jwtService;

    private static final String GOOGLE_CLIENT_ID = "97031596166-p0s80nr5ptse1sd49galglh2fnu1u8r8.apps.googleusercontent.com";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        String email = request.getEmail();

        Counselor counselor = new Counselor();
        counselor.setEmail(email);
        counselor.setName(request.getName());
        // TODO: Set other Counselor fields as needed

        counselorRepository.save(counselor);

        return ResponseEntity.ok(new AuthResponse(null, "User registered successfully"));
    }

    public ResponseEntity<AuthResponse> authenticate(AuthRequest request) {
        String email = request.getEmail();
        Counselor counselor = counselorRepository.findByEmail(email).orElse(null);

        if (counselor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "Invalid credentials"));
        }

        String token = jwtService.generateToken(counselor);
        return ResponseEntity.ok(new AuthResponse(token, "Authenticated successfully"));
    }

    public ResponseEntity<AuthResponse> googleLogin(GoogleLoginRequest request) {
        try {
            String idTokenString = request.getIdToken();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse(null, "Invalid ID token"));
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            Counselor counselor = counselorRepository.findByEmail(email).orElse(null);
            if (counselor == null) {
                counselor = new Counselor();
                counselor.setEmail(email);
                counselor.setName(name != null ? name : "Google User");
                counselorRepository.save(counselor);
            }

            String jwt = jwtService.generateToken(counselor);
            return ResponseEntity.ok(new AuthResponse(jwt, "Google login successful"));

        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(null, "Google login error: " + e.getMessage()));
        }
    }
}
