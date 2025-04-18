package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.NLPService;

@RestController
@RequestMapping("/api/nlp")
public class NLPController {

    @Autowired
    private NLPService nlpService;

    @PostMapping("/analyze")
    public String analyzeSentiment(@RequestBody String text) {
        // Call the NLPService to send the text to Flask
        return nlpService.analyzeSentiment(text);
    }
}
