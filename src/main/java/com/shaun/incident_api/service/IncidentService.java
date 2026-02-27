package com.shaun.incident_api.service;

import com.shaun.incident_api.DTO.CreateIncidentRequest;
import com.shaun.incident_api.entity.Incident;
import com.shaun.incident_api.entity.IncidentStatus;
import com.shaun.incident_api.exception.ResourceNotFoundException;
import com.shaun.incident_api.metrics.IncidentMetrics;
import com.shaun.incident_api.repository.IncidentRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class IncidentService {



    private final IncidentRepository incidentRepository;
    private final IncidentMetrics incidentMetrics;

    public IncidentService(IncidentRepository incidentRepository, IncidentMetrics incidentMetrics) {
        this.incidentRepository = incidentRepository;
        this.incidentMetrics = incidentMetrics;
//        updateMetricsAndAlert();
    }

    @PostConstruct
    public void init() {
        log.info("IncidentService initialized, updating metrics...");
        updateMetricsAndAlert();
    }

    public Incident create(CreateIncidentRequest request) {
        log.info("Creating incident with title={}", request.getTitle());
        Incident incident = new Incident();
        incident.setTitle(request.getTitle());
        incident.setSeverity(request.getSeverity());
        incident.setStatus(IncidentStatus.OPEN);
        incident.setCreatedAt(LocalDateTime.now());

        return incidentRepository.save(incident);
    }

    // Get all
    public List<Incident> getAll() {
        return incidentRepository.findAll();
    }

    // Get by id
    public Incident getById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Incident not found: " + id));
    }

    // Update status
    public Incident updateStatus(Long id, IncidentStatus status) {
        Incident incident = getById(id);
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }

    // Pagination
    public Page<Incident> getPaged(Pageable pageable) {
        return incidentRepository.findAll(pageable);
    }

    // Auto close incidents older than 7 days
    @Scheduled(cron = "0 0 1 * * ?")
    public void closeOldIncidents() {
        List<Incident> openIncidents = incidentRepository.findAll()
                .stream()
                .filter(i -> i.getStatus() == IncidentStatus.OPEN && i.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7)))
                .toList();

        openIncidents.forEach(i -> i.setStatus(IncidentStatus.CLOSED));
        incidentRepository.saveAll(openIncidents);

        log.info("Auto-closed {} incidents", openIncidents.size());
    }

    private void updateMetricsAndAlert() {
        int openCount = incidentRepository.countByStatus(IncidentStatus.OPEN);
        incidentMetrics.updateOpenIncidentCount(openCount);


        if (openCount > 20) {
            log.warn("ALERT: Open incidents exceed 20! Current count: {}", openCount);

        }
    }
}
