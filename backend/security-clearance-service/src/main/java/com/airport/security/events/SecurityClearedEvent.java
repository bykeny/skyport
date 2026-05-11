package com.airport.security.events;

public class SecurityClearedEvent {
    private Long clearanceId;
    private Long passengerId;
    private String status;

    public SecurityClearedEvent() {}

    public SecurityClearedEvent(Long clearanceId, Long passengerId, String status) {
        this.clearanceId = clearanceId;
        this.passengerId = passengerId;
        this.status = status;
    }

    public Long getClearanceId() { return clearanceId; }
    public void setClearanceId(Long clearanceId) { this.clearanceId = clearanceId; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
