package com.shaun.incident_api.repository;

import com.shaun.incident_api.entity.Incident;
import com.shaun.incident_api.entity.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    // Count incidents by status
    int countByStatus(IncidentStatus status);

    // Find stale incidents for auto-close
    List<Incident> findByStatusAndLastUpdatedBefore(IncidentStatus status, LocalDateTime time);
}
