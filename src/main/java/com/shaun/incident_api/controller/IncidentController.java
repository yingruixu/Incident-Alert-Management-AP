package com.shaun.incident_api.controller;

import com.shaun.incident_api.DTO.CreateIncidentRequest;
import com.shaun.incident_api.DTO.UpdateIncidentRequest;
import com.shaun.incident_api.entity.Incident;
import com.shaun.incident_api.entity.IncidentStatus;
import com.shaun.incident_api.exception.ResourceNotFoundException;
import com.shaun.incident_api.repository.IncidentRepository;
import com.shaun.incident_api.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentRepository incidentRepository;
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }


    // GET /incidents
    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentService.getAll();
    }

    // GET /incidents/{id}
    @GetMapping("/{id}")
    public Incident getIncidentById(@PathVariable Long id) {
        return incidentService.getById(id);
    }

    // Only ADMIN can create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Incident createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        return incidentService.create(request);
    }

    // Only ADMIN can update status
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public Incident updateStatus(
            @PathVariable Long id,
            @RequestParam IncidentStatus status) {

        return incidentService.updateStatus(id, status);
    }

    // GET /incidents/paged?page=0&size=10
    @GetMapping("/paged")
    public Page<Incident> getIncidentsPaged(
            @RequestParam int page,
            @RequestParam int size) {

        return incidentService.getPaged(PageRequest.of(page, size));
    }

    // PUT /incidents/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Incident updateIncident(
            @PathVariable Long id,
            @RequestBody UpdateIncidentRequest request) {
        return incidentService.update(id, request);
    }

    // DELETE /incidents/{id}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable Long id) {
        incidentService.delete(id);
    }

}
