package com.airport.checkin.events;

import java.time.LocalDateTime;

public class CheckInCompletedEvent {

    private Long checkInId;
    private Long passengerId;
    private Long flightId;
    private String boardingPassCode;
    private Integer baggageCount;
    private LocalDateTime occurredAt;

    public CheckInCompletedEvent() {
    }

    public CheckInCompletedEvent(Long checkInId, Long passengerId, Long flightId,
                                 String boardingPassCode, Integer baggageCount) {
        this.checkInId = checkInId;
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.boardingPassCode = boardingPassCode;
        this.baggageCount = baggageCount;
        this.occurredAt = LocalDateTime.now();
    }

    public Long getCheckInId() { return checkInId; }
    public void setCheckInId(Long checkInId) { this.checkInId = checkInId; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getBoardingPassCode() { return boardingPassCode; }
    public void setBoardingPassCode(String boardingPassCode) { this.boardingPassCode = boardingPassCode; }
    public Integer getBaggageCount() { return baggageCount; }
    public void setBaggageCount(Integer baggageCount) { this.baggageCount = baggageCount; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
