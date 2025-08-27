package com.example.newappointmentsystem.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.newappointmentsystem.models.Appointment;
import com.example.newappointmentsystem.models.Appointment.Status;
import com.example.newappointmentsystem.models.Counselor;
import com.example.newappointmentsystem.repositories.AppointmentRepository;
import com.example.newappointmentsystem.repositories.CounselorRepository;
@Service
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final AppointmentRepository appointmentRepository;

    // Make sure your constructor correctly injects AppointmentRepository
    public CounselorService(CounselorRepository counselorRepository, AppointmentRepository appointmentRepository) {
        this.counselorRepository = counselorRepository;
        this.appointmentRepository = appointmentRepository;
    }

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
    /** 
     * Retrieve a paginated list of appointments for a specific counselor, with optional filtering by month and year
     * 
     * @param counselorId The ID of the counselor
     * @param month Optional month (1-12) to filter by
     * @param year Optional year to filter by
     * @param pageable Pagination and sorting information
     * @param A page of appointment for the counselor.
     */
    //New method for filtering appointments by month and a year for a specific counselor
    public Page<Appointment>getCounselorAppointmentsByMonthAndYear(
        Long counselorId, Integer month, Integer year, Pageable pageable){
            //If  neither month nor year is provided, return all appointments for the counselor
            if (month == null && year == null){
                return appointmentRepository.findByCounselor_CounselorId(counselorId, pageable);
            }
            //If only month or only year is provided (but not both), we can't perform a percise date range
            //For this specific method, it's best to require both for 'Between' or handle invalid inputs.
            // Or, you could decide to fetch all if the combination is invalid.
            // For robustness, let's return an empty page or throw an exception if only one is provided
            if ((month != null && year == null) || (month == null && year != null)){
                return Page.empty(pageable);
            }

            //Both month and year are provided
            LocalDate startDate;
            LocalDate endDate;

            try {
                YearMonth yearMonth = YearMonth.of(year, month);
                startDate = yearMonth.atDay(1);
                endDate = yearMonth.atEndOfMonth();
            }catch (Exception e){
                //Handle invalid month/year combination if necessary, through YearMonth of validates
                //For example, if month is out of range, it will throw DateTimeException
                return Page.empty(pageable);
            }
            //This will call new method in AppointmentRepository
            return appointmentRepository.findByCounselor_CounselorIdAndAppointmentDateBetween(
               counselorId,
               startDate, 
               endDate,
               pageable
            );
        }
        /**
         * Retrieves aggregated statistics for a specific counselor'a appointments within an optional month and year range
         * 
         * @param counselorId The ID of the counselor
         * @param month Optional month (1-12) to filter by
         * @param year Optional year to filter by 
         * @return A Map containing total appointments and count by status
         */

         public Map<String,Long>getAppointmentOverviewStats(Long counselorId, Integer month, Integer year){
            LocalDate startDate;
            LocalDate endDate;

            if (month != null&& year != null){
                YearMonth yearMonth = YearMonth.of(year, month);
                startDate = yearMonth.atDay(1);
                endDate = yearMonth.atEndOfMonth();
            }else {
                startDate = LocalDate.of(1900, 1, 1);
                endDate = LocalDate.of(2100, 12, 31);
            }
            long totalAppointments = appointmentRepository.countByCounselor_CounselorIdAndAppointmentDateBetween (
                counselorId, startDate, endDate
            );

            //--UPDATED TO USE ENUM CONSTANTS--//
            long pending = appointmentRepository.countByCounselor_CounselorIdAndStatusAndAppointmentDateBetween(
                counselorId, Status.PENDING, startDate, endDate // <-- Use Status.PENDING
            );
            long confirmed = appointmentRepository.countByCounselor_CounselorIdAndStatusAndAppointmentDateBetween(
                counselorId, Status.CONFIRMED,startDate, endDate //<-- Use Status.CONFIRMED
            );
            long completed = appointmentRepository.countByCounselor_CounselorIdAndStatusAndAppointmentDateBetween(
                counselorId, Status.COMPLETED, startDate, endDate //<-- Use.Status.COMPLETED
            );
            long canceled = appointmentRepository.countByCounselor_CounselorIdAndStatusAndAppointmentDateBetween(
                counselorId, Status.CANCELED, startDate, endDate // <-- Use.Status.CANCELED
            );

            // --- END UPDATE ENUM USAGE -- //
            Map<String, Long> overviewStats = new HashMap<>();
            overviewStats.put("totalAppointments", totalAppointments);
            overviewStats.put("pendingAppointments", pending);
            overviewStats.put(" confirmedAppointmetnts", confirmed);
            overviewStats.put("completedAppointmetns", completed);
            overviewStats.put("canceledAppointments", canceled);
            
            return overviewStats;

         }
    }
        
    
