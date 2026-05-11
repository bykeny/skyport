package com.airport.flight.controller;

import com.airport.flight.dto.CreateFlightRequest;
import com.airport.flight.dto.FlightResponse;
import com.airport.flight.dto.UpdateFlightRequest;
import com.airport.flight.dto.UpdateStatusRequest;
import com.airport.flight.model.FlightStatus;
import com.airport.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Flight Scheduling", description = "Flight schedule management and status tracking")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @PostMapping("/flights")
    @Operation(summary = "Register a new flight in the schedule")
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody CreateFlightRequest req) {
        var created = service.create(req);
        return ResponseEntity.created(URI.create("/api/v1/flights/" + created.getId()))
                .body(FlightResponse.from(created));
    }

    @GetMapping("/flights/{id}")
    @Operation(summary = "Retrieve details of a specific flight")
    public FlightResponse get(@PathVariable Long id) {
        return FlightResponse.from(service.get(id));
    }

    @PutMapping("/flights/{id}")
    @Operation(summary = "Update flight information (time, aircraft, airline)")
    public FlightResponse update(@PathVariable Long id, @Valid @RequestBody UpdateFlightRequest req) {
        return FlightResponse.from(service.update(id, req));
    }

    @PatchMapping("/flights/{id}/status")
    @Operation(summary = "Update flight status (DELAYED, CANCELLED, BOARDING, etc.)")
    public FlightResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest req) {
        return FlightResponse.from(service.updateStatus(id, req));
    }

    @GetMapping("/flights")
    @Operation(summary = "List all flights, optionally filtered by date range or status")
    public List<FlightResponse> list(
            @RequestParam(required = false) FlightStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.list(status, from, to).stream().map(FlightResponse::from).toList();
    }

    @PostMapping("/flights/{id}/cancel")
    @Operation(summary = "Cancel a flight and trigger downstream notifications")
    public FlightResponse cancel(@PathVariable Long id) {
        return FlightResponse.from(service.cancel(id));
    }

    @GetMapping("/flights/search")
    @Operation(summary = "Search flights by origin, destination, and date")
    public List<FlightResponse> search(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.search(origin, destination, date).stream().map(FlightResponse::from).toList();
    }
}
