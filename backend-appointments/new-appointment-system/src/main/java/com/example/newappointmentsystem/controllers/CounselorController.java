package com.example.newappointmentsystem.controllers;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // <--- Correct import for @Valid
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.newappointmentsystem.dto.AppointmentDto;
import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.repositories.AppointmentRepository;
import com.example.newappointmentsystem.repositories.CounselorRepository;
import com.example.newappointmentsystem.services.CounselorService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    private static final Logger logger = LoggerFactory.getLogger(CounselorController.class);

    private final CounselorService counselorService;
    private final CounselorRepository counselorRepository;
    private final AppointmentRepository appointmentRepository;

    // Helper method for creating general error responses (can return String)
    private ResponseEntity<String> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }

    // Constructor for dependency injection
    public CounselorController(CounselorService counselorService,
                               CounselorRepository counselorRepository,
                               AppointmentRepository appointmentRepository) {
        this.counselorService = counselorService;
        this.counselorRepository = counselorRepository;
        this.appointmentRepository = appointmentRepository;
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

    // Endpoint: Get appointments for the authenticated counselor
    @GetMapping("/my-appointments")
    // @PreAuthorize("hasRole('COUNSELOR')") // Uncomment if you have Spring Security method security
    public ResponseEntity<Page<AppointmentDto>> getMyCounselorAppointments(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        try {
            Optional<Counselor> counselorOptional = counselorRepository.findByEmail(userDetails.getUsername());

            if (counselorOptional.isEmpty()) {
                logger.warn("Authenticated counselor with email {} not found.", userDetails.getUsername());
                return ResponseEntity.notFound().build();
            }

            Long counselorId = counselorOptional.get().getCounselorId();
            Pageable pageable = PageRequest.of(page, size);

            Page<Appointment> appointments;

            if (month != null && year != null) {
                logger.info("Fetching appointments for counselor {} by month {} and year {}, Page:{}, Size:{}",
                            counselorId, month, year, page, size);
                appointments = counselorService.getCounselorAppointmentsByMonthAndYear(counselorId, month, year, pageable);
            } else {
                logger.info("Fetching all appointments for counselor {}, Page:{}. Size {}", counselorId, page, size);
                appointments = appointmentRepository.findByCounselor_CounselorId(counselorId, pageable);
            }

            Page<AppointmentDto> appointmentDtoPage = appointments.map(AppointmentDto::new);
            return ResponseEntity.ok(appointmentDtoPage);

        } catch (Exception e) {
            logger.error("Error fetching appointments for counselor {}: {}", userDetails.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint 3: Add or update a counselor
    @PostMapping
    public ResponseEntity<?> addCounselor(@RequestBody @Valid Counselor counselor) { // <--- @Valid is here
        try {
            logger.info("Adding or updating a counselor: {}", counselor);
            Counselor savedCounselor = counselorService.saveCounselor(counselor);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCounselor);
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
            return ResponseEntity.ok(Map.of("message", message));
        } catch (RuntimeException e) {
            logger.warn("Counselor with ID {} not found: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, "Counselor with ID " + id + " not found.");
        } catch (Exception e) {
            logger.error("Error deleting counselor with ID {}: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while deleting the counselor.");
        }
    }

    // Endpoint 5: Get statistics about counselors (general count) - This will likely be replaced on frontend
    @GetMapping("/stats")
    public ResponseEntity<?> getCounselorStats() {
        try {
            logger.info("Fetching counselor statistics.");
            long totalCounselors = counselorService.getCounselorStats();
            return ResponseEntity.ok(Map.of("totalCounselors", totalCounselors));
        } catch (Exception e) {
            logger.error("Error fetching counselor stats: {}", e.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch counselor statistics.");
        }
    }

    // Endpoint: Get ALL appointments (for Admin, or general view)
    @GetMapping("/appointments")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AppointmentDto>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Fetching all appointments with pagination. Page: {}, Size: {}", page, size);
            Pageable pageable = PageRequest.of(page, size);
            Page<Appointment> appointments = counselorService.getAllAppointments(pageable);

            if (appointments.isEmpty()) {
                logger.warn("No appointments found.");
                return ResponseEntity.noContent().build();
            }

            Page<AppointmentDto> appointmentDtoPage = appointments.map(AppointmentDto::new);
            return ResponseEntity.ok(appointmentDtoPage);
        } catch (Exception e) {
            logger.error("Error fetching all appointments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- NEW ENDPOINT FOR APPOINTMENT OVERVIEW STATS ---
    @GetMapping("/appointments/overview-stats")
    public ResponseEntity<Map<String, Long>> getAppointmentOverviewStats(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        if (userDetails == null) {
            logger.warn("Unauthorized attempt to access /appointments/overview-stats (no userDetails).");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = userDetails.getUsername();
        Optional<Counselor> counselorOptional = counselorRepository.findByEmail(email);

        if (counselorOptional.isEmpty()) {
            logger.warn("Counselor not found for email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Long counselorId = counselorOptional.get().getCounselorId();

        logger.info("Fetching overview stats for counselor {} (email: {}) by month {} and year {}",
                counselorId, email, month, year);

        try {
            Map<String, Long> overviewStats = counselorService.getAppointmentOverviewStats(counselorId, month, year);
            return ResponseEntity.ok(overviewStats);
        } catch (Exception e) {
            logger.error("Error fetching overview stats for counselor {}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
} // <--- This is the ONLY closing brace for the class