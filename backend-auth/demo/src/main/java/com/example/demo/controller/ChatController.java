package com.example.demo.controller;

import com.example.demo.service.RasaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private RasaService rasaService;

    @PostMapping("/send-message")
    public String sendMessageToRasa(@RequestBody String userMessage) {
        // Send the message to Rasa and return the response
        return rasaService.getRasaResponse(userMessage);
    }
}
