package com.academic.analytics.analytics_tool.service;

import com.academic.analytics.analytics_tool.repository.AppointmentRepository;
import com.academic.analytics.analytics_tool.repository.CounselorRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CounselorRepository counselorRepository;

    public Map<String, Object> getCounselorStats() {
        long totalAppointments = appointmentRepository.count();
        long activeCounselors = counselorRepository.countByIsActive(true);
        double averageAppointmentsPerCounselor = (activeCounselors > 0)
                ? (double) totalAppointments / activeCounselors
                : 0.0;

        return Map.of(
            "totalAppointments", totalAppointments,
            "activeCounselors", activeCounselors,
            "averageAppointmentsPerCounselor", averageAppointmentsPerCounselor
        );
    }
}