package com.events.api.repositories;

import com.events.api.domain.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("SELECT e FROM Event e LEFT JOIN e.address a " +
            "WHERE (:title IS NULL OR LOWER(e.title) LIKE CONCAT('%', LOWER(:title), '%')) " +
            "AND (:city IS NULL OR LOWER(a.city) LIKE CONCAT('%', LOWER(:city), '%')) " +
            "AND (:state IS NULL OR LOWER(a.state) LIKE CONCAT('%', LOWER(:state), '%')) " +
            "AND (:startDate IS NULL OR e.date >= :startDate) " +
            "AND (:endDate IS NULL OR e.date <= :endDate) " +
            "ORDER BY e.date ASC")
    Page<Event> findFilteredEvents(@Param("title") String title,
                                   @Param("city") String city,
                                   @Param("state") String state,
                                   @Param("startDate") OffsetDateTime startDate,
                                   @Param("endDate") OffsetDateTime endDate,
                                   Pageable pageable);

    List<Event> findByIdInAndOwnerId(Collection<UUID> ids, UUID ownerId);
}
