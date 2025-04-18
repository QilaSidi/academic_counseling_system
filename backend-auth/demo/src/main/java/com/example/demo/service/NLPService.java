package com.example.demo.service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class NLPService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://127.0.0.1:5000/analyze_sentiment";

    public String analyzeSentiment(String text) {
        // Prepare request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare request body
        String requestBody = "{ \"text\": \"" + text + "\" }";

        // Build the HTTP entity (headers + body)
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Send POST request to Flask API
        ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, request, String.class);

        // Return the response body
        return response.getBody();
    }
}
