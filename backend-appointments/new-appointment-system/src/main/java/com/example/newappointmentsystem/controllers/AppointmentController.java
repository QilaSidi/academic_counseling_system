// src/main/java/com/example/newappointmentsystem/controllers/AppointmentController.java
package com.example.newappointmentsystem.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.newappointmentsystem.dto.AppointmentDto;
import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.models.Student;
import com.example.newappointmentsystem.repositories.CounselorRepository;
import com.example.newappointmentsystem.repositories.StudentRepository;
import com.example.newappointmentsystem.security.JwtUtil;
import com.example.newappointmentsystem.services.AppointmentService;
import com.example.newappointmentsystem.services.EmailService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:3000") // Adjust as needed for your frontend URL
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentService appointmentService;
    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final CounselorRepository counselorRepository;
    private final EmailService emailService;

    public AppointmentController(AppointmentService appointmentService,
                                 JwtUtil jwtUtil,
                                 StudentRepository studentRepository,
                                 CounselorRepository counselorRepository,
                                 EmailService emailService) {
        this.appointmentService = appointmentService;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.counselorRepository = counselorRepository;
        this.emailService = emailService;
    }


    // Endpoint to get all available counselors
    @GetMapping("/counselors")
    public ResponseEntity<List<Counselor>> getAllCounselors() {
        List<Counselor> counselors = appointmentService.getAllCounselors();
        return new ResponseEntity<>(counselors, HttpStatus.OK);
    }

    // Endpoint to book an appointment
    @PostMapping
    public ResponseEntity<?> createAppointment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AppointmentDto appointmentDto) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("No JWT token found in Authorization header for appointment creation.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication token missing.");
            }

            String token = authHeader.substring(7);
            logger.info("DEBUG (AppointmentController): Extracted token from header: {}", token);

            String studentEmailFromToken = jwtUtil.extractUsername(token);
            String roleFromToken = jwtUtil.extractRole(token);

            logger.info("DEBUG (AppointmentController): Extracted email from token: {}, role: {}", studentEmailFromToken, roleFromToken);

            if (!"student".equals(roleFromToken)) {
                logger.warn("Attempted appointment creation by non-student role: {}", roleFromToken);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Only students can book appointments.");
            }

            Optional<Student> studentOptional = studentRepository.findByEmail(studentEmailFromToken);

            if (studentOptional.isEmpty()) {
                logger.error("DEBUG (AppointmentController): Student not found in DB with email from token: {}", studentEmailFromToken);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Authenticated student not found in our records.");
            }

            Student student = studentOptional.get();
            String actualStudentIdFromDb = student.getStudentId();

            logger.info("DEBUG (AppointmentController): Creating appointment for student ID (from DB): {}", actualStudentIdFromDb);

            // --- START OF SERVER-SIDE VALIDATION FOR APPOINTMENT RESTRICTIONS ---

            // 1. Validate Weekday (Monday to Friday)
            LocalDate appointmentDate = appointmentDto.getAppointmentDate();
            if (appointmentDate == null) {
                logger.warn("VALIDATION FAILED: Appointment date is null.");
                return ResponseEntity.badRequest().body("Appointment date is required.");
            }
            DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                logger.warn("VALIDATION FAILED: Attempted to book on a weekend: {}", appointmentDate);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointments can only be booked on weekdays (Monday-Friday).");
            }

            // 2. Validate Time Range (9 AM to 4 PM inclusive)
            LocalTime appointmentTime = appointmentDto.getAppointmentTime();
            if (appointmentTime == null) {
                logger.warn("VALIDATION FAILED: Appointment time is null.");
                return ResponseEntity.badRequest().body("Appointment time is required.");
            }
            LocalTime startTime = LocalTime.of(9, 0); // 9:00 AM
            LocalTime endTime = LocalTime.of(16, 0); // 4:00 PM (16:00)

            if (appointmentTime.isBefore(startTime) || appointmentTime.isAfter(endTime)) {
                logger.warn("VALIDATION FAILED: Attempted to book outside allowed hours: {}", appointmentTime);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointments can only be booked between 9:00 AM and 4:00 PM.");
            }

            // --- END OF SERVER-SIDE VALIDATION ---

            // Fetch the Counselor entity using the ID from the DTO
            Counselor counselor = counselorRepository.findById(appointmentDto.getCounselorId())
                                                  .orElseThrow(() -> new RuntimeException("Counselor not found with ID: " + appointmentDto.getCounselorId()));
            
            // Call the service method to create the appointment
            Appointment createdAppointment = appointmentService.createAppointment(
                actualStudentIdFromDb,
                counselor,
                appointmentDto.getAppointmentDate(),
                appointmentDto.getAppointmentTime(),
                appointmentDto.getTopic(),
                appointmentDto.getFeedback()
            );

            // Trigger the email notification after successful appointment creation
            // We need the student's email (from token) and the counselor's email (from the fetched counselor object)
            String counselorEmail = counselor.getEmail(); // Assuming Counselor has a getEmail() method
            if (studentEmailFromToken != null && counselorEmail != null) {
                emailService.sendAppointmentStatusEmail(createdAppointment, counselorEmail, counselorEmail);
                logger.info("Email notification triggered for new appointment: {}", createdAppointment.getId());
            } else {
                logger.warn("Could not send email. Missing student or counselor email.");
            }
            
            // Return a successful response, converting the entity to a DTO
            return new ResponseEntity<>(new AppointmentDto(createdAppointment), HttpStatus.CREATED);

        } catch (UsernameNotFoundException ex) {
            logger.error("ERROR (AppointmentController): User not found during appointment creation: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            logger.error("ERROR (AppointmentController): Data integrity violation during appointment creation: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error booking appointment: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("ERROR (AppointmentController): Unhandled exception during appointment creation: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during appointment creation.");
        }
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')") // Only students can view their appointments
    public ResponseEntity<List<AppointmentDto>> getStudentAppointments(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
            }

            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String studentEmail = jwtUtil.extractUsername(jwt);

            Optional<Student> studentOptional = studentRepository.findByEmail(studentEmail);
            if (studentOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            String studentId = studentOptional.get().getStudentId();

            List<Appointment> appointments = appointmentService.getAppointmentsByStudentId(studentId);

            List<AppointmentDto> appointmentDtos = appointments.stream()
                                                               .map(AppointmentDto::new)
                                                               .collect(Collectors.toList());

            return ResponseEntity.ok(appointmentDtos);

        } catch (Exception e) {
            logger.error("Error fetching student appointments: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
