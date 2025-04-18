package com.example.newappointmentsystem.controllers;

import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.services.CounselorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.*;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    private static final Logger logger = LoggerFactory.getLogger(CounselorController.class);

    @Autowired
    private CounselorService counselorService;

    // Helper method for creating error responses
    private ResponseEntity<String> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }

    // Endpoint 1: Fetch all counselors (with pagination)
    @GetMapping
    public ResponseEntity<?> getAllCounselors(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Fetching counselors with pagination. Page: {}, Size: {}", page, size);
            Page<Counselor> counselors = counselorService.getAllCounselors(PageRequest.of(page, size));
            if (counselors.isEmpty()) {
                logger.warn("No counselors found.");
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            return ResponseEntity.ok(counselors); // Paginated counselors
        } catch (Exception e) {
            logger.error("Error fetching counselors: {}", e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch counselors.");
        }
    }

    // Endpoint 2: Fetch a specific counselor by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCounselorById(@PathVariable Long id) {
        try {
            logger.info("Fetching counselor with ID: {}", id);
            Counselor counselor = counselorService.getCounselorById(id);
            return ResponseEntity.ok(counselor);
        } catch (RuntimeException e) {
            logger.warn("Counselor with ID {} not found: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, "Counselor with ID " + id + " not found.");
        } catch (Exception e) {
            logger.error("Error fetching counselor with ID {}: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching the counselor.");
        }
    }

    // Endpoint 3: Add or update a counselor
    @PostMapping
    public ResponseEntity<?> addCounselor(@RequestBody @Valid Counselor counselor) {
        try {
            logger.info("Adding or updating a counselor: {}", counselor);
            Counselor savedCounselor = counselorService.saveCounselor(counselor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCounselor); // 201 Created
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for counselor: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid counselor data provided.");
        } catch (Exception e) {
            logger.error("Error saving counselor: {}", e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while saving the counselor.");
        }
    }

    // Endpoint 4: Delete a counselor by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCounselor(@PathVariable Long id) {
        try {
            logger.info("Deleting counselor with ID: {}", id);
            String message = counselorService.deleteCounselor(id);
            return ResponseEntity.ok(Map.of("message", message)); // JSON response with message
        } catch (RuntimeException e) {
            logger.warn("Counselor with ID {} not found: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, "Counselor with ID " + id + " not found.");
        } catch (Exception e) {
            logger.error("Error deleting counselor with ID {}: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while deleting the counselor.");
        }
    }

    // Endpoint 5: Get statistics about counselors
    @GetMapping("/stats")
    public ResponseEntity<?> getCounselorStats() {
        try {
            logger.info("Fetching counselor statistics.");
            long totalCounselors = counselorService.getCounselorStats();
            return ResponseEntity.ok(Map.of("totalCounselors", totalCounselors)); // JSON response with stats
        } catch (Exception e) {
            logger.error("Error fetching counselor stats: {}", e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch counselor statistics.");
        }
    }
    @GetMapping("/appointments")
public ResponseEntity<?> getAppointments(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
    try {
        logger.info("Fetching appointments with pagination. Page: {}, Size: {}", page, size);
        Page<Appointment> appointments = counselorService.getAllAppointments(PageRequest.of(page, size)); // Service method
        if (appointments.isEmpty()) {
            logger.warn("No appointments found.");
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(appointments); // Paginated appointments
    } catch (Exception e) {
        logger.error("Error fetching appointments: {}", e.getMessage());
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch appointments.");
    }
}
    
}