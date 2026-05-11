package com.airport.flight.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UpdateFlightRequest {

    @NotNull
    @Future
    private LocalDateTime scheduledDeparture;

    @NotNull
    @Future
    private LocalDateTime scheduledArrival;

    private String aircraftType;
    private String airlineCode;

    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(LocalDateTime scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }
    public LocalDateTime getScheduledArrival() { return scheduledArrival; }
    public void setScheduledArrival(LocalDateTime scheduledArrival) { this.scheduledArrival = scheduledArrival; }
    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }
    public String getAirlineCode() { return airlineCode; }
    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }
}
