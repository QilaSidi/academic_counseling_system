// src/main/java/com/example/newappointmentsystem/dto/AppointmentDto.java
package com.example.newappointmentsystem.dto;

import java.time.LocalDate; // Import the entity
import java.time.LocalTime; // Keep this if you need to pass Counselor objects in the DTO for POST

import com.example.newappointmentsystem.models.Appointment;

public class AppointmentDto {

    private String appointmentId; // Changed to String to match DTO field type
    private String studentId;
    private String studentFirstName; // Using firstName from Student entity
    private String studentLastName;  // Using lastName from Student entity
    private String studentEmail;
    private Long counselorId; // Changed to Long to match Counselor entity ID type
    private String counselorFirstName; // Using firstName from Counselor entity
    private String counselorLastName;  // Using lastName from Counselor entity
    private String counselorEmail;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String topic; // Added topic field as per your entity
    private String status; // Status will be a String representation of the enum
    private String feedback;

    // --- MANDATORY: No-argument constructor for Jackson deserialization (for POST requests) ---
    public AppointmentDto() {
    }

    // --- NEW: Constructor to map from Appointment entity to AppointmentDto ---
    // This constructor is used when converting Page<Appointment> to Page<AppointmentDto>
    public AppointmentDto(Appointment appointment) {
        // Map appointment ID (Long from entity, String in DTO)
        this.appointmentId = String.valueOf(appointment.getId()); // Corrected: Use getId() from Appointment entity

        // Map the nested Student entity details
        if (appointment.getStudent() != null) {
            this.studentId = appointment.getStudent().getStudentId();
            this.studentFirstName = appointment.getStudent().getFirstName(); // Corrected: Use getFirstName()
            this.studentLastName = appointment.getStudent().getLastName();   // Corrected: Use getLastName()
            this.studentEmail = appointment.getStudent().getEmail();
        }

        // Map the nested Counselor entity details
        if (appointment.getCounselor() != null) {
            this.counselorId = appointment.getCounselor().getCounselorId(); // Corrected: Counselor ID is Long
            this.counselorFirstName = appointment.getCounselor().getFirstName(); // Corrected: Use getFirstName()
            this.counselorLastName = appointment.getCounselor().getLastName();   // Corrected: Use getLastName()
            this.counselorEmail = appointment.getCounselor().getEmail();
        }

        this.appointmentDate = appointment.getAppointmentDate();
        this.appointmentTime = appointment.getAppointmentTime();
        this.topic = appointment.getTopic(); // Corrected: Use getTopic() from Appointment entity
        this.status = appointment.getStatus() != null ? appointment.getStatus().name() : null; // Corrected: Convert Enum to String
        this.feedback = appointment.getFeedback();
    }

    // --- Optional: Parameterized constructor for creating DTOs manually (e.g., from frontend input) ---
    // Note: For POST requests, Spring usually uses the no-arg constructor + setters,
    // or a constructor with @JsonCreator if you have complex deserialization logic.
    // This constructor might be used if you construct DTOs programmatically on the backend.
    public AppointmentDto(String studentId, Long counselorId, LocalDate appointmentDate, LocalTime appointmentTime, String topic, String feedback) {
        this.studentId = studentId;
        this.counselorId = counselorId; // Counselor ID is Long
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.topic = topic;
        this.feedback = feedback;
        this.status = Appointment.Status.PENDING.name(); // Default status for new appointments
    }


    // --- Getters and Setters for all fields (ensure they are public) ---
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentFirstName() { return studentFirstName; }
    public void setStudentFirstName(String studentFirstName) { this.studentFirstName = studentFirstName; }

    public String getStudentLastName() { return studentLastName; }
    public void setStudentLastName(String studentLastName) { this.studentLastName = studentLastName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public Long getCounselorId() { return counselorId; } // Getter for Long counselorId
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; } // Setter for Long counselorId

    public String getCounselorFirstName() { return counselorFirstName; }
    public void setCounselorFirstName(String counselorFirstName) { this.counselorFirstName = counselorFirstName; }

    public String getCounselorLastName() { return counselorLastName; }
    public void setCounselorLastName(String counselorLastName) { this.counselorLastName = counselorLastName; }

    public String getCounselorEmail() { return counselorEmail; }
    public void setCounselorEmail(String counselorEmail) { this.counselorEmail = counselorEmail; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}