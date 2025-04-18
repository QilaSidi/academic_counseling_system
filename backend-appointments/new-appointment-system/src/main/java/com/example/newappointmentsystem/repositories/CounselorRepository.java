package com.example.newappointmentsystem.repositories;

import com.example.newappointmentsystem.models.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    Optional<Counselor> findByFirstNameAndLastName(String firstName, String lastName);
    Optional<Counselor> findByCounselorId(Long counselorId);
    Optional<Counselor> findByEmail(String email);
}