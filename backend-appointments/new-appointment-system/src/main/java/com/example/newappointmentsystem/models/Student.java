package com.example.newappointmentsystem.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id", length = 20, nullable = false)
    private String studentId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "current_semester")
    private Integer currentSemester;

    @Column(name= "course_enrolled", length =255)
    private String courseEnrolled;

    // ---NEW FIELD: Course Code---
    @Column (name= "course_code", length = 20)
    private String courseCode;

    // --- Constructors ---
    public Student() {
        // Default constructor required by JPA
    }

    // THIS IS THE CORRECTED CONSTRUCTOR
    // Notice that Integer currentYear and String courseEnrolled are now IN THE PARENTHESES
    public Student(String studentId, String firstName, String lastName, String email,
                   String phoneNumber, LocalDate dateOfBirth, LocalDateTime createdAt,
                   Integer currentSemester, String courseEnrolled, String courseCode) { // <--- THEY ARE HERE NOW!
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.currentSemester = currentSemester;    // Now this 'currentYear' refers to the parameter
        this.courseEnrolled = courseEnrolled; // Now this 'courseEnrolled' refers to the parameter
        this.courseCode = courseCode;
    }

    // --- Getters and Setters ---
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentYear(Integer currentSemester){
        this.currentSemester = currentSemester;
    }

    public String getCourseEnrolled(){
        return courseEnrolled;
    }
    public void setCourseEnrolled(String courseEnrolled) {
        this.courseEnrolled = courseEnrolled;
    }

    // --- NEW Getters and Setters for Course Code---
    public String getCourseCode(){
        return courseCode;
    }

    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", createdAt=" + createdAt +
                ", currentYear=" + currentSemester +
                ", courseEnrolled='" + courseEnrolled +'\''+
                ", courseCode='" + courseCode + '\'' +
                '}';
    }
}
