package com.shaun.incident_api.DTO;

import com.shaun.incident_api.entity.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public class CreateIncidentRequest {

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotNull(message = "severity is required")
    private Severity severity;

    public @NotBlank(message = "title must not be blank") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "title must not be blank") String title) {
        this.title = title;
    }

    public @NotNull(message = "severity is required") Severity getSeverity() {
        return severity;
    }

    public void setSeverity(@NotNull(message = "severity is required") Severity severity) {
        this.severity = severity;
    }
}
