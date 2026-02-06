package com.shaun.incident_api.controller;

import com.shaun.incident_api.entity.Incident;
import com.shaun.incident_api.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // GET /incidents
    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    // GET /incidents/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return incidentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /incidents
    @PostMapping
    public Incident createIncident(@RequestBody Incident incident) {
        return incidentRepository.save(incident);
    }

    // PATCH /incidents/{id}?status=CLOSED
    @PatchMapping("/{id}")
    public ResponseEntity<Incident> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return incidentRepository.findById(id).map(incident -> {
            incident.setStatus(status);
            incidentRepository.save(incident);
            return ResponseEntity.ok(incident);
        }).orElse(ResponseEntity.notFound().build());
    }

    //Close incident day > 7
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(fixedRate = 10000) // 每10秒执行一次
    public void closeOldIncidents() {
        List<Incident> openIncidents = incidentRepository.findAll()
                .stream()
                .filter(i -> i.getStatus().equals("OPEN") &&
                        i.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7)))
                .toList();

        for (Incident i : openIncidents) {
            i.setStatus("CLOSED");
        }

        incidentRepository.saveAll(openIncidents);
        System.out.println("Auto-closed " + openIncidents.size() + " incidents");
    }

    @GetMapping("/paged")
    public Page<Incident> getIncidentsPaged(
            @RequestParam int page,
            @RequestParam int size) {
        return incidentRepository.findAll(PageRequest.of(page, size));
    }

}
