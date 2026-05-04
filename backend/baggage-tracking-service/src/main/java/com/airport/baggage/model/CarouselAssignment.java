package com.airport.baggage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carousel_assignments")
public class CarouselAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "baggage_id", nullable = false)
    private BaggageItem baggageItem;

    @Column(nullable = false)
    private String carouselNumber;

    private LocalDateTime assignedAt;
    private LocalDateTime arrivedAt;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BaggageItem getBaggageItem() { return baggageItem; }
    public void setBaggageItem(BaggageItem baggageItem) { this.baggageItem = baggageItem; }
    public String getCarouselNumber() { return carouselNumber; }
    public void setCarouselNumber(String carouselNumber) { this.carouselNumber = carouselNumber; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getArrivedAt() { return arrivedAt; }
    public void setArrivedAt(LocalDateTime arrivedAt) { this.arrivedAt = arrivedAt; }
}
