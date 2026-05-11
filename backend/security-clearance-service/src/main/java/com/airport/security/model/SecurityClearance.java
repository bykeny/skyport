package com.airport.security.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_clearances")
public class SecurityClearance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long passengerId;

    private Long checkInId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClearanceStatus status;

    private String screeningZone;
    private LocalDateTime clearedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = ClearanceStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status == ClearanceStatus.CLEARED && clearedAt == null) {
            clearedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getCheckInId() { return checkInId; }
    public void setCheckInId(Long checkInId) { this.checkInId = checkInId; }
    public ClearanceStatus getStatus() { return status; }
    public void setStatus(ClearanceStatus status) { this.status = status; }
    public String getScreeningZone() { return screeningZone; }
    public void setScreeningZone(String screeningZone) { this.screeningZone = screeningZone; }
    public LocalDateTime getClearedAt() { return clearedAt; }
    public void setClearedAt(LocalDateTime clearedAt) { this.clearedAt = clearedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
