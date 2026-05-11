package com.airport.security.dto;

import com.airport.security.model.ClearanceStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateClearanceStatusRequest {

    @NotNull(message = "Status is required")
    private ClearanceStatus status;

    public ClearanceStatus getStatus() { return status; }
    public void setStatus(ClearanceStatus status) { this.status = status; }
}
