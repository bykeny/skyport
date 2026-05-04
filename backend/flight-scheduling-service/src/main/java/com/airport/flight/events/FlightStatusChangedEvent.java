package com.airport.flight.events;
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
    public String getFlightNumber() { return flightNumber; }
    public FlightStatus getNewStatus() { return newStatus; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
}
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
    public String getFlightNumber() { return flightNumber; }
    public FlightStatus getNewStatus() { return newStatus; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
}
