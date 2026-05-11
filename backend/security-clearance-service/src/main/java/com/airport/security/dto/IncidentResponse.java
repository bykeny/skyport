package com.airport.security.dto;

import java.time.LocalDateTime;

public class IncidentResponse {
    private Long id;
    private Long clearanceId;
    private String severity;
    private String description;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClearanceId() { return clearanceId; }
    public void setClearanceId(Long clearanceId) { this.clearanceId = clearanceId; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
