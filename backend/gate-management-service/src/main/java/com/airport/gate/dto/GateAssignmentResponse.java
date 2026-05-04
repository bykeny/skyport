package com.airport.gate.dto;
package com.airport.gate.dto;

import com.airport.gate.model.GateAssignment;

import java.time.LocalDateTime;

public class GateAssignmentResponse {
    private Long id;
    private Long gateId;
    private String gateNumber;
    private Long flightId;
    private LocalDateTime assignedAt;
    private LocalDateTime releasedAt;
    private Integer durationMinutes;

    public static GateAssignmentResponse from(GateAssignment a) {
        GateAssignmentResponse r = new GateAssignmentResponse();
        r.id = a.getId();
        r.gateId = a.getGate().getId();
        r.gateNumber = a.getGate().getGateNumber();
        r.flightId = a.getFlightId();
        r.assignedAt = a.getAssignedAt();
        r.releasedAt = a.getReleasedAt();
        r.durationMinutes = a.getDurationMinutes();
        return r;
    }

    public Long getId() { return id; }
    public Long getGateId() { return gateId; }
    public String getGateNumber() { return gateNumber; }
    public Long getFlightId() { return flightId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public LocalDateTime getReleasedAt() { return releasedAt; }
    public Integer getDurationMinutes() { return durationMinutes; }
}
import com.airport.gate.model.GateAssignment;

import java.time.LocalDateTime;

public class GateAssignmentResponse {
    private Long id;
    private Long gateId;
    private String gateNumber;
    private Long flightId;
    private LocalDateTime assignedAt;
    private LocalDateTime releasedAt;
    private Integer durationMinutes;

    public static GateAssignmentResponse from(GateAssignment a) {
        GateAssignmentResponse r = new GateAssignmentResponse();
        r.id = a.getId();
        r.gateId = a.getGate().getId();
        r.gateNumber = a.getGate().getGateNumber();
        r.flightId = a.getFlightId();
        r.assignedAt = a.getAssignedAt();
        r.releasedAt = a.getReleasedAt();
        r.durationMinutes = a.getDurationMinutes();
        return r;
    }

    public Long getId() { return id; }
    public Long getGateId() { return gateId; }
    public String getGateNumber() { return gateNumber; }
    public Long getFlightId() { return flightId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public LocalDateTime getReleasedAt() { return releasedAt; }
    public Integer getDurationMinutes() { return durationMinutes; }
}
