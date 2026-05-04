package com.airport.baggage.dto;

import jakarta.validation.constraints.NotBlank;

public class CarouselAssignmentRequest {

    @NotBlank(message = "Carousel number is required")
    private String carouselNumber;

    public String getCarouselNumber() { return carouselNumber; }
    public void setCarouselNumber(String carouselNumber) { this.carouselNumber = carouselNumber; }
}
