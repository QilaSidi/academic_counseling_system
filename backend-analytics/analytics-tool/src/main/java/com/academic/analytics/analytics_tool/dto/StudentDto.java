// src/main/java/com/academic/analytics/analytics_tool/dto/StudentDto.java
package com.academic.analytics.analytics_tool.dto;

import com.academic.analytics.analytics_tool.model.Student; // Import the Student model from this project

public class StudentDto {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dateOfBirth; // String for transfer
    private String createdAt;   // String for transfer

    // --- ACADEMIC FIELDS ---
    private Integer currentSemester;
    private String courseEnrolled;
    private String courseCode;

    // No-arg constructor for deserialization (if frontend sends this DTO)
    public StudentDto() {
    }

    // Constructor to map from Student entity to StudentDto
    public StudentDto(Student student) {
        this.studentId = student.getStudentId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.phoneNumber = student.getPhoneNumber();
        this.dateOfBirth = (student.getDateOfBirth() != null) ? student.getDateOfBirth().toString() : null;
        this.createdAt = (student.getCreatedAt() != null) ? student.getCreatedAt().toString() : null;
        this.currentSemester = student.getCurrentSemester();
        this.courseEnrolled = student.getCourseEnrolled();
        this.courseCode = student.getCourseCode();
    }

    // --- Getters and Setters ---
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public Integer getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(Integer currentSemester) { this.currentSemester = currentSemester; }

    public String getCourseEnrolled() { return courseEnrolled; }
    public void setCourseEnrolled(String courseEnrolled) { this.courseEnrolled = courseEnrolled; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
}
