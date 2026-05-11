package com.airport.security.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_incidents")
public class SecurityIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clearance_id", nullable = false)
    private SecurityClearance clearance;

    private String severity;
    private String description;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SecurityClearance getClearance() { return clearance; }
    public void setClearance(SecurityClearance clearance) { this.clearance = clearance; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
