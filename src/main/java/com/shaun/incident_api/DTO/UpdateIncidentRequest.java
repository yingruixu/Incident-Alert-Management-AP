package com.shaun.incident_api.DTO;

import com.shaun.incident_api.entity.IncidentStatus;
import com.shaun.incident_api.entity.Severity;

public class UpdateIncidentRequest {

    private String title;
    private Severity severity;
    private IncidentStatus status;
    private String description;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}