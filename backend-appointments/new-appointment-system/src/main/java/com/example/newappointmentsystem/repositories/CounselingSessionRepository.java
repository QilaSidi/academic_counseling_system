package com.example.newappointmentsystem.repositories;

import com.example.newappointmentsystem.models.CounselingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselingSessionRepository extends JpaRepository<CounselingSession, Long> {
    List<CounselingSession> findByStudentId(String studentId);
}


