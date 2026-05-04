package com.airport.checkin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BaggageDropRequest {

    @NotNull(message = "Baggage count is required")
    @Min(value = 1, message = "Baggage count must be at least 1")
    private Integer baggageCount;

    public Integer getBaggageCount() { return baggageCount; }
    public void setBaggageCount(Integer baggageCount) { this.baggageCount = baggageCount; }
}
