package com.airport.baggage.events;

public class BaggageRegisteredEvent {
    private Long baggageId;
    private Long passengerId;
    private Long flightId;
    private String tagNumber;

    public BaggageRegisteredEvent() {}

    public BaggageRegisteredEvent(Long baggageId, Long passengerId, Long flightId, String tagNumber) {
        this.baggageId = baggageId;
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.tagNumber = tagNumber;
    }

    public Long getBaggageId() { return baggageId; }
    public void setBaggageId(Long baggageId) { this.baggageId = baggageId; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getTagNumber() { return tagNumber; }
    public void setTagNumber(String tagNumber) { this.tagNumber = tagNumber; }
}
