package com.airport.gate.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gates")
public class Gate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String gateNumber;

    @Column(nullable = false, length = 8)
    private String terminal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private GateType gateType = GateType.NARROW_BODY;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private GateStatus status = GateStatus.AVAILABLE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getGateNumber() { return gateNumber; }
    public void setGateNumber(String gateNumber) { this.gateNumber = gateNumber; }
    public String getTerminal() { return terminal; }
    public void setTerminal(String terminal) { this.terminal = terminal; }
    public GateType getGateType() { return gateType; }
    public void setGateType(GateType gateType) { this.gateType = gateType; }
    public GateStatus getStatus() { return status; }
    public void setStatus(GateStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
