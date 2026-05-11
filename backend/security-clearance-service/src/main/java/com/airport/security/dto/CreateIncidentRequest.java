package com.airport.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateIncidentRequest {

    @NotNull(message = "Clearance ID is required")
    private Long clearanceId;

    @NotBlank(message = "Severity is required")
    private String severity;

    private String description;

    public Long getClearanceId() { return clearanceId; }
    public void setClearanceId(Long clearanceId) { this.clearanceId = clearanceId; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}