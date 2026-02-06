package com.shaun.incident_api.controller;

import com.shaun.incident_api.DTO.CreateAlertRequest;
import com.shaun.incident_api.entity.Alert;
import com.shaun.incident_api.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public Alert create(@Valid @RequestBody CreateAlertRequest req) {
        return alertService.create(
                req.getSource(),
                req.getMessage(),
                req.getSeverity()
        );
    }

    @GetMapping
    public List<Alert> list() {
        return alertService.findAll();
    }
}
