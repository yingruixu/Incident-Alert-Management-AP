package com.shaun.incident_api.DTO;


import jakarta.validation.constraints.NotBlank;

public class CreateAlertRequest {

    @NotBlank
    private String source;

    @NotBlank
    private String message;

    @NotBlank
    private String severity;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
