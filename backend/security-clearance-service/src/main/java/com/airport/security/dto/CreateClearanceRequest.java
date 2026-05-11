package com.airport.security.dto;

import jakarta.validation.constraints.NotNull;

public class CreateClearanceRequest {

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;
    private Long checkInId;
    private String screeningZone;

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getCheckInId() { return checkInId; }
    public void setCheckInId(Long checkInId) { this.checkInId = checkInId; }
    public String getScreeningZone() { return screeningZone; }
    public void setScreeningZone(String screeningZone) { this.screeningZone = screeningZone; }
}
