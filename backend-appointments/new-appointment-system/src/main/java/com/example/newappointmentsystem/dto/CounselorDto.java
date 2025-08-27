// src/main/java/com/example/newappointmentsystem/dto/CounselorDto.java
package com.example.newappointmentsystem.dto;

public class CounselorDto {
    private Long counselorId; // You might want to include the ID for actions
    private String firstName;
    private String lastName;
    private String email; // Include other relevant fields

    // Constructor to map from Counselor entity
    public CounselorDto(Long counselorId, String firstName, String lastName, String email) {
        this.counselorId = counselorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    // Getters and Setters
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}