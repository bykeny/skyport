package com.airport.checkin.events;

import com.airport.checkin.model.CheckInStatus;
import java.time.LocalDateTime;

public class CheckInStatusChangedEvent {

    private Long checkInId;
    private Long passengerId;
    private Long flightId;
    private CheckInStatus status;
    private LocalDateTime occurredAt;

    public CheckInStatusChangedEvent() {
    }

    public CheckInStatusChangedEvent(Long checkInId, Long passengerId, Long flightId, CheckInStatus status) {
        this.checkInId = checkInId;
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.status = status;
        this.occurredAt = LocalDateTime.now();
    }

    public Long getCheckInId() { return checkInId; }
    public void setCheckInId(Long checkInId) { this.checkInId = checkInId; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public CheckInStatus getStatus() { return status; }
    public void setStatus(CheckInStatus status) { this.status = status; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}
