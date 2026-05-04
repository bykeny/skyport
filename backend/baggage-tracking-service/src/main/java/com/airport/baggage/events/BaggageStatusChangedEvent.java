package com.airport.baggage.events;

import com.airport.baggage.model.BaggageStatus;

public class BaggageStatusChangedEvent {
    private Long baggageId;
    private Long passengerId;
    private BaggageStatus newStatus;
    private String tagNumber;

    public BaggageStatusChangedEvent() {}

    public BaggageStatusChangedEvent(Long baggageId, Long passengerId, BaggageStatus newStatus, String tagNumber) {
        this.baggageId = baggageId;
        this.passengerId = passengerId;
        this.newStatus = newStatus;
        this.tagNumber = tagNumber;
    }

    public Long getBaggageId() { return baggageId; }
    public void setBaggageId(Long baggageId) { this.baggageId = baggageId; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public BaggageStatus getNewStatus() { return newStatus; }
    public void setNewStatus(BaggageStatus newStatus) { this.newStatus = newStatus; }
    public String getTagNumber() { return tagNumber; }
    public void setTagNumber(String tagNumber) { this.tagNumber = tagNumber; }
}
