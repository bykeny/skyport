package com.airport.checkin.integration;

public interface FlightValidationClient {

    void validateFlightExists(Long flightId);
}
