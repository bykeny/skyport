package com.airport.gate.events;

import java.time.LocalDateTime;

public class FlightStatusChangedEvent {

    private Long flightId;
    private String flightNumber;
    private String newStatus;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime actualDeparture;

    public FlightStatusChangedEvent() {}

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(LocalDateTime scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public void setActualDeparture(LocalDateTime actualDeparture) { this.actualDeparture = actualDeparture; }
}
