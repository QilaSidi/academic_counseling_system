package com.example.newappointmentsystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.newappointmentsystem.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> { // <--- CHANGE Long TO String HERE

    Optional<Student> findByEmail(String email);

    Optional<Student> findByStudentId(String studentId); // If studentId is the primary key, this might be redundant with findById
}