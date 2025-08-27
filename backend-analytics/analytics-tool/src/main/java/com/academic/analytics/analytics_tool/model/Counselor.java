package com.academic.analytics.analytics_tool.model;

import jakarta.persistence.*; // Use jakarta.persistence
import java.time.LocalDateTime;

@Entity
@Table(name = "counselors")
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for BIGINT (assuming it's BIGINT now)
    @Column(name = "counselor_id")
    private Long counselorId; // Use Long for BIGINT

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "expertise", length = 255)
    private String expertise;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Assuming you might have these columns from previous updates, if not, remove them
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "name", length = 255) // Assuming 'name' column exists
    private String name;

    @Column(name = "role", length = 50) // Assuming 'role' column exists
    private String role;


    // --- Constructors ---
    public Counselor() {}

    // Minimal constructor
    public Counselor(Long counselorId, String firstName, String lastName, String email) {
        this.counselorId = counselorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Full constructor (adjust as per your actual needs)
    public Counselor(Long counselorId, String firstName, String lastName, String email, String phoneNumber, String expertise, LocalDateTime createdAt, Boolean isActive, String name, String role) {
        this.counselorId = counselorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.expertise = expertise;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.name = name;
        this.role = role;
    }

    // --- Getters and Setters ---
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "Counselor{" +
               "counselorId=" + counselorId +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
