package com.airport.retail.client;

public interface CheckInValidationClient {
    Long validatePassengerCheckedIn(Long passengerId, Long flightId);
}
