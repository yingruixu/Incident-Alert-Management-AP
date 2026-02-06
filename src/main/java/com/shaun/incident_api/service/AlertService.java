package com.shaun.incident_api.service;

import com.shaun.incident_api.entity.Alert;
import com.shaun.incident_api.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert create(String source, String message, String severity) {
        Alert alert = new Alert();
        alert.setSource(source);
        alert.setMessage(message);
        alert.setSeverity(severity);
        return alertRepository.save(alert);
    }

    public List<Alert> findAll() {
        return alertRepository.findAll();
    }
}
