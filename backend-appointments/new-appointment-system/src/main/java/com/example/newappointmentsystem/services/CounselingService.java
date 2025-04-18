package com.example.newappointmentsystem.services;

import com.example.newappointmentsystem.models.CounselingSession;
import com.example.newappointmentsystem.repositories.CounselingSessionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CounselingService {

    @Autowired
    private CounselingSessionRepository counselingSessionRepository;

    // Retrieve counseling history for a specific student ID
    public List<CounselingSession> getHistoryByStudentId(String studentId) {
        return counselingSessionRepository.findByStudentId(studentId); // Now accepts String ✅
    }
}
