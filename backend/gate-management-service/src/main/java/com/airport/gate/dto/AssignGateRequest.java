package com.airport.gate.dto;
package com.airport.gate.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AssignGateRequest {
    @NotNull
    private Long gateId;
    @NotNull
    private Long flightId;

    private LocalDateTime assignedAt;

    public Long getGateId() { return gateId; }
    public void setGateId(Long gateId) { this.gateId = gateId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AssignGateRequest {
    @NotNull
    private Long gateId;
    @NotNull
    private Long flightId;

    private LocalDateTime assignedAt;

    public Long getGateId() { return gateId; }
    public void setGateId(Long gateId) { this.gateId = gateId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
