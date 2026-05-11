package com.airport.security.dto;

import com.airport.security.model.ClearanceStatus;
import java.time.LocalDateTime;

public class ClearanceResponse {
    private Long id;
    private Long passengerId;
    private Long checkInId;
    private ClearanceStatus status;
    private String screeningZone;
    private LocalDateTime clearedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
