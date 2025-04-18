package com.example.demo.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class RasaService {

    private final String rasaUrl = "http://localhost:5005/webhooks/rest/webhook"; // Rasa's endpoint
    private final RestTemplate restTemplate = new RestTemplate(); // RestTemplate instance

    // Method to communicate with Rasa and get the response
    public String getRasaResponse(String userMessage) {
        // Create JSON body
        String jsonBody = "{\"sender\": \"test_user\", \"message\": \"" + userMessage + "\"}";

        // Log the request body before sending it
        System.out.println("Request body: " + jsonBody);

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
        // Create the HTTP entity with body and headers
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        
        // Send the request and get the response from Rasa
        ResponseEntity<String> response = restTemplate.exchange(rasaUrl, HttpMethod.POST, entity, String.class);

        // Return the response body from Rasa
        return response.getBody();
    }
}


