package com.academic.analytics.analytics_tool.model;

import jakarta.persistence.*; // Use jakarta.persistence
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for BIGINT
    @Column(name = "appointment_id")
    private Long appointmentId; // Use Long for BIGINT

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime; // Use LocalTime for TIME

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate; // Use LocalDate for DATE

    @Column(name = "counselor_id", nullable = false)
    private Long counselorId; // Use Long for BIGINT

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Use LocalDateTime for DATETIME(6)

    @Column(name = "feedback", length = 255)
    private String feedback;

    @Enumerated(EnumType.STRING) // Store enum as String in DB
    @Column(name = "status", nullable = false)
    private AppointmentStatus status; // Custom enum for status

    @Column(name = "student_id", length = 20, nullable = false)
    private String studentId; // Use String for VARCHAR(20)

    // Many-to-one relationship with Counselor (optional, for direct fetching)
    // If you always join in service/repository, this might not be strictly needed here
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", referencedColumnName = "counselor_id", insertable = false, updatable = false)
    private Counselor counselor; // To get counselor details directly

    // --- Constructors ---
    public Appointment() {}

    // Constructor without ID (for new appointments)
    public Appointment(LocalTime appointmentTime, LocalDate appointmentDate, Long counselorId, LocalDateTime createdAt, String feedback, AppointmentStatus status, String studentId) {
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
        this.counselorId = counselorId;
        this.createdAt = createdAt;
        this.feedback = feedback;
        this.status = status;
        this.studentId = studentId;
    }

    // --- Getters and Setters ---
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Counselor getCounselor() { return counselor; }
    public void setCounselor(Counselor counselor) { this.counselor = counselor; }

    @Override
    public String toString() {
        return "Appointment{" +
               "appointmentId=" + appointmentId +
               ", appointmentTime=" + appointmentTime +
               ", appointmentDate=" + appointmentDate +
               ", counselorId=" + counselorId +
               ", createdAt=" + createdAt +
               ", feedback='" + feedback + '\'' +
               ", status=" + status +
               ", studentId='" + studentId + '\'' +
               '}';
    }
}
