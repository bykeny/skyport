package com.airport.checkin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "passenger_checkins",
        uniqueConstraints = @UniqueConstraint(columnNames = {"passengerId", "flightId"})
)
public class PassengerCheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long passengerId;

    @Column(nullable = false)
    private Long flightId;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false, unique = true)
    private String boardingPassCode;

    private Integer baggageCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckInStatus status;

    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        checkedInAt = now;
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = CheckInStatus.CHECKED_IN;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
