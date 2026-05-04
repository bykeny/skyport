package com.airport.checkin.controller;

import com.airport.checkin.dto.BaggageDropRequest;
import com.airport.checkin.dto.CheckInResponse;
import com.airport.checkin.dto.CreateCheckInRequest;
import com.airport.checkin.dto.UpdateCheckInStatusRequest;
import com.airport.checkin.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkins")
@Tag(name = "Passenger Check-in", description = "Passenger check-in and boarding pass management")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping
    @Operation(summary = "Create a passenger check-in")
    public ResponseEntity<CheckInResponse> createCheckIn(@Valid @RequestBody CreateCheckInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkInService.createCheckIn(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get check-in by ID")
    public ResponseEntity<CheckInResponse> getCheckIn(@PathVariable Long id) {
        return ResponseEntity.ok(checkInService.getCheckIn(id));
    }

    @GetMapping("/passenger/{passengerId}/flight/{flightId}")
    @Operation(summary = "Get a passenger check-in for a flight")
    public ResponseEntity<CheckInResponse> getPassengerFlightCheckIn(@PathVariable Long passengerId,
                                                                     @PathVariable Long flightId) {
        return ResponseEntity.ok(checkInService.getPassengerFlightCheckIn(passengerId, flightId));
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "List check-ins for a flight")
    public ResponseEntity<List<CheckInResponse>> getFlightCheckIns(@PathVariable Long flightId) {
        return ResponseEntity.ok(checkInService.getFlightCheckIns(flightId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update check-in status")
    public ResponseEntity<CheckInResponse> updateStatus(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateCheckInStatusRequest request) {
        return ResponseEntity.ok(checkInService.updateStatus(id, request));
    }

    @PostMapping("/{id}/baggage-drop")
    @Operation(summary = "Record checked baggage drop-off")
    public ResponseEntity<CheckInResponse> recordBaggageDrop(@PathVariable Long id,
                                                             @Valid @RequestBody BaggageDropRequest request) {
        return ResponseEntity.ok(checkInService.recordBaggageDrop(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a passenger check-in")
    public ResponseEntity<CheckInResponse> cancelCheckIn(@PathVariable Long id) {
        return ResponseEntity.ok(checkInService.cancelCheckIn(id));
    }
}
