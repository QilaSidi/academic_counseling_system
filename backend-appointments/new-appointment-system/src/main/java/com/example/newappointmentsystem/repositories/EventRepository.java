package com.example.newappointmentsystem.repositories;

import com.example.newappointmentsystem.models.Event;  // Import the Event entity
import org.springframework.data.jpa.repository.JpaRepository; // Import JpaRepository
import org.springframework.stereotype.Repository; // Import Repository annotation
import java.util.List;  // Import List

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findTop5ByOrderByEventDateDesc();  // Get the latest 5 events
}
