package com.airport.gate.events;
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
    public String getFlightNumber() { return flightNumber; }
    public String getNewStatus() { return newStatus; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
}
import java.time.LocalDateTime;

public class FlightStatusChangedEvent {
    private Long flightId;
    private String flightNumber;
    private String newStatus;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime actualDeparture;

    public FlightStatusChangedEvent() {}

    public Long getFlightId() { return flightId; }
    public String getFlightNumber() { return flightNumber; }
    public String getNewStatus() { return newStatus; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
}
