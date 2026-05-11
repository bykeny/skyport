package com.airport.gate.events;

import com.airport.gate.model.GateStatus;

public class GateUpdatedEvent {

    private Long gateId;
    private String gateNumber;
    private GateStatus status;
    private Long flightId;

    public GateUpdatedEvent() {}

    public GateUpdatedEvent(Long gateId, String gateNumber, GateStatus status, Long flightId) {
        this.gateId = gateId;
        this.gateNumber = gateNumber;
        this.status = status;
        this.flightId = flightId;
    }

    public Long getGateId() { return gateId; }
    public void setGateId(Long gateId) { this.gateId = gateId; }
    public String getGateNumber() { return gateNumber; }
    public void setGateNumber(String gateNumber) { this.gateNumber = gateNumber; }
    public GateStatus getStatus() { return status; }
    public void setStatus(GateStatus status) { this.status = status; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
}
