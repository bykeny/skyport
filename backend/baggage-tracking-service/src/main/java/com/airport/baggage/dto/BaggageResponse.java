package com.airport.baggage.dto;

import com.airport.baggage.model.BaggageStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BaggageResponse {
    private Long id;
    private Long passengerId;
    private Long flightId;
    private String tagNumber;
    private String barcode;
    private LocalDateTime issuedAt;
    private BigDecimal weightValue;
    private String weightUnit;
    private BaggageStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getTagNumber() { return tagNumber; }
    public void setTagNumber(String tagNumber) { this.tagNumber = tagNumber; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    public BigDecimal getWeightValue() { return weightValue; }
    public void setWeightValue(BigDecimal weightValue) { this.weightValue = weightValue; }
    public String getWeightUnit() { return weightUnit; }
    public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
    public BaggageStatus getStatus() { return status; }
    public void setStatus(BaggageStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
