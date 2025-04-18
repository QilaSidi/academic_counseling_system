package com.academic.analytics.analytics_tool.repository;

import com.academic.analytics.analytics_tool.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Built-in methods from JpaRepository
}