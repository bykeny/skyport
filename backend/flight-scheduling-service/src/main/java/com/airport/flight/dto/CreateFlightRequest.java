package com.airport.flight.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class CreateFlightRequest {

    @NotBlank
    private String flightNumber;

    @NotBlank
    @Size(min = 3, max = 3, message = "origin must be a 3-letter airport code, for example TLL")
    private String origin;

    @NotBlank
    @Size(min = 3, max = 3, message = "destination must be a 3-letter airport code, for example IST")
    private String destination;

    @NotNull
    @Future
    private LocalDateTime scheduledDeparture;

    @NotNull
    @Future
    private LocalDateTime scheduledArrival;

    private String aircraftType;
    private String airlineCode;

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(LocalDateTime scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }
    public LocalDateTime getScheduledArrival() { return scheduledArrival; }
    public void setScheduledArrival(LocalDateTime scheduledArrival) { this.scheduledArrival = scheduledArrival; }
    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }
    public String getAirlineCode() { return airlineCode; }
    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }
}
