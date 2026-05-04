package com.airport.checkin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCheckInRequest {

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @Min(value = 0, message = "Baggage count cannot be negative")
    private Integer baggageCount;

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public Integer getBaggageCount() { return baggageCount; }
    public void setBaggageCount(Integer baggageCount) { this.baggageCount = baggageCount; }
}
