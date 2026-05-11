package com.airport.gate.dto;

import com.airport.gate.model.Gate;
import com.airport.gate.model.GateStatus;
import com.airport.gate.model.GateType;
import java.time.LocalDateTime;

public class GateResponse {

    private Long id;
    private String gateNumber;
    private String terminal;
    private GateType gateType;
    private GateStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GateResponse from(Gate g) {
        GateResponse r = new GateResponse();
        r.id = g.getId();
        r.gateNumber = g.getGateNumber();
        r.terminal = g.getTerminal();
        r.gateType = g.getGateType();
        r.status = g.getStatus();
        r.createdAt = g.getCreatedAt();
        r.updatedAt = g.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getGateNumber() { return gateNumber; }
    public String getTerminal() { return terminal; }
    public GateType getGateType() { return gateType; }
    public GateStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
