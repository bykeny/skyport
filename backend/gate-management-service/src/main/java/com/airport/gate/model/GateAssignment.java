package com.airport.gate.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "gate_assignments")
public class GateAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_id")
    private Gate gate;

    @Column(nullable = false)
    private Long flightId;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    private LocalDateTime releasedAt;

    private Integer durationMinutes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }

    public void releaseNow() {
        this.releasedAt = LocalDateTime.now();
        if (assignedAt != null) {
            this.durationMinutes = (int) Duration.between(assignedAt, releasedAt).toMinutes();
        }
    }

    public Long getId() { return id; }
    public Gate getGate() { return gate; }
    public void setGate(Gate gate) { this.gate = gate; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getReleasedAt() { return releasedAt; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
