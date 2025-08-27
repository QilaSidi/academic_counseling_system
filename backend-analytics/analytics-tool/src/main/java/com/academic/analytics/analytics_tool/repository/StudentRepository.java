package com.academic.analytics.analytics_tool.repository;
// Ensure this matches the folder structure

import java.util.Optional; // Ensure this matches your Student model's location in this project

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academic.analytics.analytics_tool.model.Student;

@Repository // Marks this interface as a Spring Data JPA repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // JpaRepository provides common CRUD operations (save, findById, findAll, delete, etc.)
    // The first generic type is the entity (Student), the second is the type of its primary key (String, based on your studentId)

    /**
     * Custom query method to find a Student by their email address.
     * Spring Data JPA automatically implements this method based on its naming convention.
     * @param email The email address to search for.
     * @return An Optional containing the found Student, or empty if not found.
     */
    Optional<Student> findByEmail(String email);

    // If your Student entity's primary key is 'studentId' (String),
    // findById(String) would already be available from JpaRepository.
    // You might have this if you have another field named studentId that is not the @Id:
    // Optional<Student> findByStudentId(String studentId);
}