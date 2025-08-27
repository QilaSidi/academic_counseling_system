package com.example.newappointmentsystem.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    // REMOVE THE DUPLICATE STRING studentId FIELD IF you are using the Student object relationship
    // @NotNull(message = "Student ID is required.")
    // @Column(name = "student_id", nullable = false, length = 20)
    // private String studentId; // DELETE THIS LINE

    // Keep this one, as it's the ManyToOne relationship to the Student entity
    @ManyToOne // Establishes a many-to-one relationship with Student
    @JoinColumn(name = "student_id", nullable = false) // Foreign key column
    private Student student; // This maps to your Student entity

    // Consolidate 'appointmentDate' and 'date' into one field
    @NotNull(message = "Appointment date is required.")
    @Future(message = "Appointment date must be in the future.")
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate; // Keep this name, or rename to 'date' if preferred.
                                      // I'll assume you prefer 'appointmentDate' since it's already used.

    // Consolidate 'appointmentTime' and 'time' into one field
    @NotNull(message = "Appointment time is required.")
    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime; // Keep this name, or rename to 'time' if preferred.

    @ManyToOne
    @JoinColumn(name = "counselor_id", nullable = false)
    private Counselor counselor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING; // Initializing PENDING is good

    private String feedback; // Keep this field

    private String confirmationToken;

     @Column(name = "topic") // You might want to add nullable = false if topic is mandatory
    private String topic;

    // ADD THESE GETTER AND SETTER METHODS FOR 'topic':
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    // Enum for Appointment Status - This looks good!
    public enum Status {
        PENDING,
        COMPLETED,
        CONFIRMED,
        CANCELED
    }

    // --- IMPORTANT: Adjust Getters and Setters based on the consolidated fields ---

    // Getters and Setters for ID (Already correct)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // NEW/REVISED: Getter and Setter for the Student object
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    // REVISED: Getter and Setter for Appointment Date
    public LocalDate getAppointmentDate() { // Use 'appointmentDate' consistently
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) { // Use 'appointmentDate' consistently
        this.appointmentDate = appointmentDate;
    }

    // REVISED: Getter and Setter for Appointment Time
    public LocalTime getAppointmentTime() { // Use 'appointmentTime' consistently
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) { // Use 'appointmentTime' consistently
        this.appointmentTime = appointmentTime;
    }


    // Getters and Setters for Counselor (Already correct)
    public Counselor getCounselor() {
        return counselor;
    }

    public void setCounselor(Counselor counselor) {
        this.counselor = counselor;
    }

    // Getters and Setters for Status (Already correct and uses the enum type)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Getters and Setters for Feedback (Already correct)
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    //Getters and setters for confirmationToken
    public String getConfirmationToken(){
        return confirmationToken;
    }
    public void setConfirmationToken(String confirmationToken){
        this.confirmationToken= confirmationToken;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", student=" + (student != null ? student.getStudentId() : "null") + // Reference student's ID or email
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                 ", topic='" + topic + '\'' + // Make sure to include topic in toString as well
                ", counselor=" + (counselor != null ? counselor.getFirstName() + " " + counselor.getLastName() : "null") +
                ", status=" + status +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}