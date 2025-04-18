package com.example.newappointmentsystem.services;

import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.repositories.CounselorRepository;
import com.example.newappointmentsystem.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounselorService {

    @Autowired
    private CounselorRepository counselorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Fetch all counselors (pagination)
    public Page<Counselor> getAllCounselors(Pageable pageable) {
        return counselorRepository.findAll(pageable);
    }

    // Fetch all counselors (non-paginated)
    public List<Counselor> getAllCounselors() {
        return counselorRepository.findAll();
    }

    // Fetch a specific counselor by ID
    public Counselor getCounselorById(Long counselorId) {
        return counselorRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Counselor not found with ID: " + counselorId));
    }

    // Add or update a counselor
    public Counselor saveCounselor(Counselor counselor) {
        return counselorRepository.save(counselor);
    }

    // Delete a counselor by ID
    public String deleteCounselor(Long counselorId) {
        if (!counselorRepository.existsById(counselorId)) {
            throw new RuntimeException("Counselor not found with ID: " + counselorId);
        }
        counselorRepository.deleteById(counselorId);
        return "Counselor with ID " + counselorId + " has been deleted successfully.";
    }

    // Get statistics about counselors
    public long getCounselorStats() {
        return counselorRepository.count();
    }

    // Fetch all appointments (pagination)
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }
}