package com.academic.analytics.analytics_tool.controller;

import com.academic.analytics.analytics_tool.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/counselor-stats")
    public ResponseEntity<?> getCounselorStats() {
        try {
            Map<String, Object> stats = analyticsService.getCounselorStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while fetching counselor statistics.");
        }
    }
}