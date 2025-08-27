package com.example.newappointmentsystem.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.newappointmentsystem.exceptions.ResourceNotFoundException;
import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Appointment.Status;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.models.Student;
import com.example.newappointmentsystem.repositories.AppointmentRepository;
import com.example.newappointmentsystem.repositories.CounselorRepository;
import com.example.newappointmentsystem.repositories.StudentRepository;


@Service
public class AppointmentService {
    //Add a new dependency for the EmailService
    private final EmailService emailService;

     // Declare repositories as final
    private final AppointmentRepository appointmentRepository;
    private final CounselorRepository counselorRepository;
    private final StudentRepository studentRepository;

    @Autowired // @Autowired on constructor is optional if there's only one constructor in Spring Boot 2.x+, but good for clarity
    public AppointmentService(AppointmentRepository appointmentRepository,
                              CounselorRepository counselorRepository,
                              StudentRepository studentRepository,//add EmailService to the constructor
                              EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.counselorRepository = counselorRepository;
        this.studentRepository = studentRepository;
        this.emailService = emailService;
    }
     public List<Counselor> getAllCounselors() {
        return counselorRepository.findAll();
    }
    @Transactional
    public Appointment createAppointment(
            String studentId,
            Counselor counselor,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String topic,
            String feedback) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        //Counselor counselor = counselorRepository.findById(counselorId)
                //.orElseThrow(() -> new ResourceNotFoundException("Counselor not found with ID: " + counselorId));

        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setCounselor(counselor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setFeedback(feedback);
        appointment.setStatus(Status.PENDING);
        appointment.setConfirmationToken(UUID.randomUUID().toString());
        
        //Save the appointment to the database first to ensure it has a valid ID
        Appointment savedAppointment = appointmentRepository.save(appointment);

        //Call the email service after the appointment has been saved
        emailService.sendAppointmentStatusEmail(appointment, studentId, studentId); 

        return savedAppointment;
    }

    // Make sure THIS METHOD IS PRESENT and matches this signature:
    public List<Appointment> getAppointmentsByStudentId(String studentId) {
        // FIX THIS LINE: Use findByStudent_StudentIdOrderBy...
        return appointmentRepository.findByStudent_StudentIdOrderByAppointmentDateAscAppointmentTimeAsc(studentId);
    }

    // You can add more service methods here (e.g., getAppointmentsByStudent, updateAppointmentStatus, etc.)
}