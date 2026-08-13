package com.events.api.repositories;

import com.events.api.domain.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("SELECT e FROM Event e LEFT JOIN e.address a WHERE e.date >= :currentDate")
    Page<Event> findUpcomingEvents(@Param("currentDate") OffsetDateTime currentDate, Pageable pageable);

    @Query("SELECT e FROM Event e LEFT JOIN e.address a " +
            "WHERE (:title IS NULL OR e.title LIKE %:title%) " +
            "AND (:city IS NULL OR a.city LIKE %:city%) " +
            "AND (:state IS NULL OR a.state LIKE %:state%) " +
            "AND (e.date >= :startDate AND e.date <= :endDate)")
    Page<Event> findFilteredEvents(@Param("title") String title,
                                   @Param("city") String city,
                                   @Param("state") String state,
                                   @Param("startDate") OffsetDateTime startDate,
                                   @Param("endDate") OffsetDateTime endDate,
                                   Pageable pageable);
}
