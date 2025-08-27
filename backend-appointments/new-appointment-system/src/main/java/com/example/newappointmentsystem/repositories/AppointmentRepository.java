package com.example.newappointmentsystem.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;         // <--- ADD THIS IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Appointment.Status;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // CHANGE THIS LINE:
    // Old: List<Appointment> findByStudentId(String studentId);
    // New: Use the property name of the Student object (student) followed by the ID property name within Student (StudentId)
    List<Appointment> findByStudent_StudentId(String studentId); // THIS IS THE CORRECTION


    // This will automatically create the query to find appointments by studentId
    // and order them by date and time in ascending order.
   List<Appointment> findByStudent_StudentIdOrderByAppointmentDateAscAppointmentTimeAsc(String studentId);
    // Explanation:
    // - `findBy`: Standard Spring Data JPA prefix
    // - `Student`: Refers to the `private Student student;` field in your Appointment entity.
    // - `_`: This is the crucial separator that tells Spring Data JPA to "go into" the `student` object.
    // - `StudentId`: Refers to the `private String studentId;` field (the primary key) *within* the Student entity.
    // - `OrderByAppointmentDateAscAppointmentTimeAsc`: Your existing ordering.
    // IMPORTANT: Add @EntityGraph to ensure Student and Counselor are fetched
       @EntityGraph(attributePaths = {"student", "counselor"}) // Or whatever your @EntityGraph attributes are
    Page<Appointment> findByCounselor_CounselorId(Long counselorId, Pageable pageable);

    //New method to find appointments by counselor ID, month, and year
    @EntityGraph(attributePaths = {"student", "counselor"})
    Page<Appointment>findByCounselor_CounselorIdAndAppointmentDateBetween(
        Long counselorId, LocalDate startDate, LocalDate endDate, Pageable pageable);

        // ---NEW METHOD FOR APPOINTMENT OVERVIEW STATS ---
        /**
         * Counts the total number of appointments for a specific counselor within a given data range
         * @param counselorId the ID of the counselor,
         * @param startDate The start date of the range (inclusive)
         * @param endDate The end date of the range (inclusive)
         * @return The total count of appointments
         */
        Long countByCounselor_CounselorIdAndAppointmentDateBetween(Long counselorId, LocalDate startDate, LocalDate endDate);

        /**
         * Count the number of appointments for a specific counselor withtin given status within a specified date range.
         * @param counselorID the ID of the counselor
         * @param status The status of the appointment (e.g., "PENDING","COMPLETED"
         * @param startDate the start date of the range (inclusive)
         * @param endDate The end date of the range (inclusive)
         * @return The count of appointment matching the criteria
         */
        Long countByCounselor_CounselorIdAndStatusAndAppointmentDateBetween(Long counselorId, Status status, LocalDate startDate, LocalDate endDate);



}
    
