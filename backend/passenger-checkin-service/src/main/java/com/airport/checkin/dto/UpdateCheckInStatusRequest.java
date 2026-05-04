package com.airport.checkin.dto;

import com.airport.checkin.model.CheckInStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateCheckInStatusRequest {

    @NotNull(message = "Status is required")
    private CheckInStatus status;

    public CheckInStatus getStatus() { return status; }
    public void setStatus(CheckInStatus status) { this.status = status; }
}
