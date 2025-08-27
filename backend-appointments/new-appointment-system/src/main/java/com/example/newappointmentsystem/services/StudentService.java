package com.example.newappointmentsystem.services;

import java.util.Optional; // Ensure this import points to your copied Student model

import org.springframework.beans.factory.annotation.Autowired; // Ensure this import points to your copied StudentRepository
import org.springframework.stereotype.Service;

import com.example.newappointmentsystem.models.Student;
import com.example.newappointmentsystem.repositories.StudentRepository; // Important: import Optional

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Constructor injection (optional, but good practice)
    // public StudentService(StudentRepository studentRepository) {
    //     this.studentRepository = studentRepository;
    // }

    // This method is what AppointmentController is looking for
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email); // Assuming findByEmail exists in StudentRepository
    }

    // Add other student-related methods you might have copied, e.g.,
    // public Optional<Student> getStudentById(String studentId) {
    //     return studentRepository.findByStudentId(studentId);
    // }

    // public Student saveStudent(Student student) {
    //     return studentRepository.save(student);
    // }
}