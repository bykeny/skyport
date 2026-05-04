package com.airport.baggage.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class RegisterBaggageRequest {

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    private BigDecimal weightValue;
    private String weightUnit;

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public BigDecimal getWeightValue() { return weightValue; }
    public void setWeightValue(BigDecimal weightValue) { this.weightValue = weightValue; }
    public String getWeightUnit() { return weightUnit; }
    public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
}
