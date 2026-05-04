package com.airport.flight.controller;

import com.airport.flight.dto.CreateFlightRequest;
import com.airport.flight.dto.FlightResponse;
import com.airport.flight.dto.UpdateFlightRequest;
import com.airport.flight.dto.UpdateStatusRequest;
import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;
import com.airport.flight.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @PostMapping("/flights")
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody CreateFlightRequest req) {
        Flight created = service.create(req);
        return ResponseEntity.created(URI.create("/api/v1/flights/" + created.getId()))
                .body(FlightResponse.from(created));
    }

    @GetMapping("/flights/{id}")
    public FlightResponse get(@PathVariable Long id) {
        return FlightResponse.from(service.get(id));
    }

    @PutMapping("/flights/{id}")
    public FlightResponse update(@PathVariable Long id, @Valid @RequestBody UpdateFlightRequest req) {
        return FlightResponse.from(service.update(id, req));
    }

    @PatchMapping("/flights/{id}/status")
    public FlightResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest req) {
        return FlightResponse.from(service.updateStatus(id, req));
    }

    @GetMapping("/flights")
    public List<FlightResponse> list(
            @RequestParam(required = false) FlightStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return service.list(status, from, to).stream().map(FlightResponse::from).toList();
    }

    @PostMapping("/flights/{id}/cancel")
    public FlightResponse cancel(@PathVariable Long id) {
        return FlightResponse.from(service.cancel(id));
    }
    @GetMapping("/flights/search")
    public List<FlightResponse> search(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return service.search(origin, destination, date).stream().map(FlightResponse::from).toList();
    }
}
