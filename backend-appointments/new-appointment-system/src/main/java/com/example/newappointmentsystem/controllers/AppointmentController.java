package com.example.newappointmentsystem.controllers;

import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.dto.AppointmentDto;
import com.example.newappointmentsystem.services.AppointmentService;
import com.example.newappointmentsystem.services.CounselorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CounselorService counselorService;

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        logger.info("Test endpoint accessed.");
        return ResponseEntity.ok("AppointmentController is working!");
    }

    // Fetch all counselors
    @GetMapping("/counselors")
    public ResponseEntity<List<Counselor>> getCounselors() {
        try {
            List<Counselor> counselors = counselorService.getAllCounselors(); // No pagination
            return ResponseEntity.ok(counselors);
        } catch (Exception e) {
            logger.error("Error fetching counselors: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Fetch all appointments or filter by studentId
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(
            @RequestParam(value = "studentId", required = false) String studentId) {
        try {
            List<Appointment> appointments = (studentId != null && !studentId.isEmpty())
                    ? appointmentService.getAppointmentsByStudentId(studentId)
                    : appointmentService.getAllAppointments();

            if (appointments.isEmpty()) {
                logger.warn("No appointments found.");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            logger.error("Error fetching appointments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create a new appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        try {
            Appointment createdAppointment = appointmentService.createAppointment(appointmentDto);
            logger.info("Appointment created successfully with ID: {}", createdAppointment.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Error creating appointment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Fetch appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        try {
            return appointmentService.getAppointmentById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("Appointment with ID: {} not found.", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error fetching appointment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete an appointment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            logger.info("Appointment with ID: {} deleted successfully.", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid ID provided: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error deleting appointment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}              