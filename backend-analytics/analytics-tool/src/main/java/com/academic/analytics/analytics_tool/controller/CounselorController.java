package com.academic.analytics.analytics_tool.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/counselor") // ✅ This must match what your frontend calls

public class CounselorController {

    @GetMapping("/appointments")
    public ResponseEntity<List<Map<String, String>>> getAppointments() {
        // Mock data (Replace with actual DB call later)
        List<Map<String, String>> appointments = List.of(
            Map.of("id", "1", "studentName", "John Doe", "date", "2025-04-01"),
            Map.of("id", "2", "studentName", "Jane Doe", "date", "2025-04-02")
        );

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Integer>> getStats() {
        // Mock data (Replace with actual analytics logic later)
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalAppointments", 10);
        stats.put("activeCounselors", 5);

        return ResponseEntity.ok(stats);
    }
}
