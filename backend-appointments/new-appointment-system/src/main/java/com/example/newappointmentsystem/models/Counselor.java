package com.example.newappointmentsystem.models;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "counselors")
public class Counselor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorId; 

    private String firstName;
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;
    private String expertise;
    
    @Column(nullable = false)
    private String password; // Store hashed password

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Getters and Setters
    public Long getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(Long counselorId) {
        this.counselorId = counselorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getPassword() {
        return password;
    }

    // Hash password before saving
    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    // Verify password
    public boolean checkPassword(String rawPassword) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
