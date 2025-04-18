package com.academic.analytics.analytics_tool.repository;

import com.academic.analytics.analytics_tool.model.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Import Optional

public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    long countByIsActive(boolean isActive); // Custom method to count active counselors
    Optional<Counselor> findByEmail(String email); // This will now resolve correctly
}
