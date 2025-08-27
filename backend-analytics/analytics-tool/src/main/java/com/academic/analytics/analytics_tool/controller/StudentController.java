// src/main/java/com/academic/analytics/analytics_tool/controller/StudentController.java
package com.academic.analytics.analytics_tool.controller;

import java.util.Optional; // No longer strictly needed for getStudentDashboardData, but might be used elsewhere

import org.springframework.http.HttpStatus;   // No longer strictly needed for getStudentDashboardData, but might be used elsewhere
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academic.analytics.analytics_tool.dto.StudentDto;
import com.academic.analytics.analytics_tool.model.Student;
import com.academic.analytics.analytics_tool.security.JwtUtil;
import com.academic.analytics.analytics_tool.service.StudentService;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class StudentController {

    private final JwtUtil jwtUtil;
    private final StudentService studentService;

    public StudentController(JwtUtil jwtUtil, StudentService studentService) {
        this.jwtUtil = jwtUtil;
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    // FIX: Changed return type to ResponseEntity<StudentDto>
    public ResponseEntity<StudentDto> getStudentDashboardData(@RequestHeader("Authorization") String authorizationHeader) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            System.out.println("DEBUG: Extracted token from header: " + token);
        } else {
            System.out.println("DEBUG: Authorization header is missing or malformed.");
        }

        if (token == null) {
            // FIX: Return type changed, so body must match. Returning null for StudentDto on error.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);
            System.out.println("DEBUG: Extracted email from token: " + email);
            System.out.println("DEBUG: Extracted role from token: " + role);

            if (!"student".equals(role)) {
                System.out.println("DEBUG: Role mismatch. Expected 'student', got: " + role);
                // FIX: Return type changed, so body must match. Returning null for StudentDto on error.
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            Optional<Student> studentOptional = studentService.getStudentByEmail(email);

            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();
                // FIX: Create a StudentDto object from the Student entity
                StudentDto studentDto = new StudentDto(student);

                System.out.println("DEBUG: Student data found for email: " + email);
                // FIX: Return the StudentDto
                return ResponseEntity.ok(studentDto);
            } else {
                System.out.println("DEBUG: Student data NOT found in DB for email: " + email);
                // FIX: Return type changed, so body must match. Returning null for StudentDto on error.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (Exception e) {
            System.err.println("ERROR: Token validation or extraction failed: " + e.getMessage());
            e.printStackTrace();
            // FIX: Return type changed, so body must match. Returning null for StudentDto on error.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // This is the endpoint that StudentDashboard.js was initially trying to call
    // Keeping it as is, but current frontend logic calls /student/dashboard
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
