package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
public class Sophie {

    @GetMapping("/chat")
    public Map<String, String> chat(@RequestParam String message) {
        // Process the message and respond accordingly
        String responseMessage = getChatbotResponse(message);
        Map<String, String> response = new HashMap<>();
        response.put("response", responseMessage);
        return response;
    }

    private String getChatbotResponse(String message) {
        if (message.toLowerCase().contains("appointment")) {
            return "I can help you with booking an appointment. Please provide the date and time.";
        } else if (message.toLowerCase().contains("mental health")) {
            return "It's important to take care of your mental health. If you're feeling down, try to talk to a counselor.";
        } else {
            return "Sorry, I didn't understand that. Can you please clarify?";
        }
    }
}
