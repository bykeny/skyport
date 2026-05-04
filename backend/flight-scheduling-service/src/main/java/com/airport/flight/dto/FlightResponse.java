package com.airport.flight.dto;
package com.airport.flight.dto;

import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;

import java.time.LocalDateTime;

public class FlightResponse {
    private Long id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private LocalDateTime actualDeparture;
    private FlightStatus status;
    private String aircraftType;
    private String airlineCode;

    public static FlightResponse from(Flight f) {
        FlightResponse r = new FlightResponse();
        r.id = f.getId();
        r.flightNumber = f.getFlightNumber();
        r.origin = f.getOrigin();
        r.destination = f.getDestination();
        r.scheduledDeparture = f.getScheduledDeparture();
        r.scheduledArrival = f.getScheduledArrival();
        r.actualDeparture = f.getActualDeparture();
        r.status = f.getStatus();
        r.aircraftType = f.getAircraftType();
        r.airlineCode = f.getAirlineCode();
        return r;
    }

    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getScheduledArrival() { return scheduledArrival; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public FlightStatus getStatus() { return status; }
    public String getAircraftType() { return aircraftType; }
    public String getAirlineCode() { return airlineCode; }
}
import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;

import java.time.LocalDateTime;

public class FlightResponse {
    private Long id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private LocalDateTime actualDeparture;
    private FlightStatus status;
    private String aircraftType;
    private String airlineCode;

    public static FlightResponse from(Flight f) {
        FlightResponse r = new FlightResponse();
        r.id = f.getId();
        r.flightNumber = f.getFlightNumber();
        r.origin = f.getOrigin();
        r.destination = f.getDestination();
        r.scheduledDeparture = f.getScheduledDeparture();
        r.scheduledArrival = f.getScheduledArrival();
        r.actualDeparture = f.getActualDeparture();
        r.status = f.getStatus();
        r.aircraftType = f.getAircraftType();
        r.airlineCode = f.getAirlineCode();
        return r;
    }

    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getScheduledArrival() { return scheduledArrival; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public FlightStatus getStatus() { return status; }
    public String getAircraftType() { return aircraftType; }
    public String getAirlineCode() { return airlineCode; }
}
