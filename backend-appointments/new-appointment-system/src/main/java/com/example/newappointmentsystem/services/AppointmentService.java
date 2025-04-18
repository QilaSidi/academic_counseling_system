package com.example.newappointmentsystem.services;

import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.dto.AppointmentDto;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CounselorService counselorService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByStudentId(String studentId) {
        return appointmentRepository.findByStudentId(studentId);
    }

    public Appointment createAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        appointment.setStudentId(appointmentDto.getStudentId());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());
        Counselor counselor = counselorService.getCounselorById(appointmentDto.getCounselorId());
        appointment.setCounselor(counselor);
        appointment.setFeedback(appointmentDto.getFeedback());
        appointment.setStatus(Appointment.Status.PENDING);
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}