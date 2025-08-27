package com.academic.analytics.analytics_tool.service; // This is the correct package for THIS project

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.academic.analytics.analytics_tool.model.Student;
import com.academic.analytics.analytics_tool.repository.StudentRepository; // <--- THIS IS THE MISSING IMPORT for THIS project


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // @Autowired // Removed: Autowired is not strictly necessary for single constructor injection
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves student data by email.
     * @param email The email of the student.
     * @return An Optional containing the Student object if found, or empty if not found.
     */
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

     public Student getStudentById(String studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        return studentOptional.orElse(null); // Or throw a specific NotFoundException
    }
}