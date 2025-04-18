package com.example.newappointmentsystem.controllers;

import com.example.newappointmentsystem.models.CounselingSession;
import com.example.newappointmentsystem.services.CounselingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Collections;

@RestController
@RequestMapping("/counseling-history")
public class CounselorHistoryController {

    @Autowired
    private CounselingService counselingService;

    @GetMapping
    public ResponseEntity<List<CounselingSession>> getCounselingHistory(@RequestParam String studentId) {
        // Fetch history from the service
        List<CounselingSession> history = counselingService.getHistoryByStudentId(studentId);

        // Handle empty history gracefully
        if (history.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // Return empty list ✅
        }

        return ResponseEntity.ok(history);
    }
}
