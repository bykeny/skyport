package com.airport.flight.events;

import com.airport.flight.model.FlightStatus;
import java.time.LocalDateTime;

public class FlightStatusChangedEvent {

    private Long flightId;
    private String flightNumber;
    private FlightStatus newStatus;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime actualDeparture;

    public FlightStatusChangedEvent() {}

    public FlightStatusChangedEvent(Long flightId, String flightNumber, FlightStatus newStatus,
                                    LocalDateTime scheduledDeparture, LocalDateTime actualDeparture) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.newStatus = newStatus;
        this.scheduledDeparture = scheduledDeparture;
        this.actualDeparture = actualDeparture;
    }

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public FlightStatus getNewStatus() { return newStatus; }
    public void setNewStatus(FlightStatus newStatus) { this.newStatus = newStatus; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(LocalDateTime scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public void setActualDeparture(LocalDateTime actualDeparture) { this.actualDeparture = actualDeparture; }
}
