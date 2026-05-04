package com.airport.checkin.dto;

import com.airport.checkin.model.CheckInStatus;
import java.time.LocalDateTime;

public class CheckInResponse {

    private Long id;
    private Long passengerId;
    private Long flightId;
    private String seatNumber;
    private String boardingPassCode;
    private Integer baggageCount;
    private CheckInStatus status;
    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getBoardingPassCode() { return boardingPassCode; }
    public void setBoardingPassCode(String boardingPassCode) { this.boardingPassCode = boardingPassCode; }
    public Integer getBaggageCount() { return baggageCount; }
    public void setBaggageCount(Integer baggageCount) { this.baggageCount = baggageCount; }
    public CheckInStatus getStatus() { return status; }
    public void setStatus(CheckInStatus status) { this.status = status; }
    public LocalDateTime getCheckedInAt() { return checkedInAt; }
    public void setCheckedInAt(LocalDateTime checkedInAt) { this.checkedInAt = checkedInAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
