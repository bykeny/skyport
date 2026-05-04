package com.airport.baggage.dto;

import com.airport.baggage.model.BaggageStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateBaggageStatusRequest {

    @NotNull(message = "Status is required")
    private BaggageStatus status;

    public BaggageStatus getStatus() { return status; }
    public void setStatus(BaggageStatus status) { this.status = status; }
}
