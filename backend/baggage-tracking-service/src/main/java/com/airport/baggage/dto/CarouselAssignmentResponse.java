package com.airport.baggage.dto;

import java.time.LocalDateTime;

public class CarouselAssignmentResponse {
    private Long id;
    private Long baggageItemId;
    private String carouselNumber;
    private LocalDateTime assignedAt;
    private LocalDateTime arrivedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBaggageItemId() { return baggageItemId; }
    public void setBaggageItemId(Long baggageItemId) { this.baggageItemId = baggageItemId; }
    public String getCarouselNumber() { return carouselNumber; }
    public void setCarouselNumber(String carouselNumber) { this.carouselNumber = carouselNumber; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getArrivedAt() { return arrivedAt; }
    public void setArrivedAt(LocalDateTime arrivedAt) { this.arrivedAt = arrivedAt; }
}
