package com.academic.analytics.analytics_tool.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "feedback_id", nullable = false)
        private Integer feedbackId;

        @Column(name = "appointment_id")  // BIGINT, nullable
        private Long appointmentId;

        @Column(name = "student_id", length = 20)  // VARCHAR(20), nullable
        private String studentId;

        @Column(name = "counselor_id")  // BIGINT, nullable
        private Long counselorId;

        @Column(name = "rating")
        private Integer rating;

        @Column(name = "comment", columnDefinition = "TEXT")
        private String comment;

        // Getters and Setters...
        public Integer getFeedbackId() { return feedbackId; }
        public void setFeedbackId(Integer feedbackId) { this.feedbackId = feedbackId; }

        public Long getAppointmentId() { return appointmentId; }
        public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }

        public Long getCounselorId() { return counselorId; }
        public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
}