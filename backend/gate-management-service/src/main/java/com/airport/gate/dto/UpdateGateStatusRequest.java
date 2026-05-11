package com.airport.gate.dto;

import com.airport.gate.model.GateStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateGateStatusRequest {

    @NotNull
    private GateStatus status;

    public GateStatus getStatus() { return status; }
    public void setStatus(GateStatus status) { this.status = status; }
}
