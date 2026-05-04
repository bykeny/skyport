package com.airport.baggage.controller;

import com.airport.baggage.dto.*;
import com.airport.baggage.service.BaggageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/baggage")
@Tag(name = "Baggage Tracking", description = "Baggage lifecycle management")
public class BaggageController {

    private final BaggageService baggageService;

    public BaggageController(BaggageService baggageService) {
        this.baggageService = baggageService;
    }

    @PostMapping
    @Operation(summary = "Register a new baggage item")
    public ResponseEntity<BaggageResponse> registerBaggage(@Valid @RequestBody RegisterBaggageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(baggageService.registerBaggage(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get baggage by ID")
    public ResponseEntity<BaggageResponse> getBaggage(@PathVariable Long id) {
        return ResponseEntity.ok(baggageService.getBaggageById(id));
    }

    @GetMapping("/tag/{tagNumber}")
    @Operation(summary = "Get baggage by tag number")
    public ResponseEntity<BaggageResponse> getBaggageByTag(@PathVariable String tagNumber) {
        return ResponseEntity.ok(baggageService.getBaggageByTag(tagNumber));
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "List all baggage for a passenger")
    public ResponseEntity<List<BaggageResponse>> getBaggageByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(baggageService.getBaggageByPassenger(passengerId));
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "List all baggage for a flight")
    public ResponseEntity<List<BaggageResponse>> getBaggageByFlight(@PathVariable Long flightId) {
        return ResponseEntity.ok(baggageService.getBaggageByFlight(flightId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update baggage status")
    public ResponseEntity<BaggageResponse> updateStatus(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateBaggageStatusRequest request) {
        return ResponseEntity.ok(baggageService.updateStatus(id, request));
    }

    @PostMapping("/{id}/carousel")
    @Operation(summary = "Assign baggage to arrival carousel")
    public ResponseEntity<CarouselAssignmentResponse> assignCarousel(@PathVariable Long id,
                                                                      @Valid @RequestBody CarouselAssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(baggageService.assignCarousel(id, request));
    }
}
