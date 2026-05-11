package com.airport.gate.controller;

import com.airport.gate.dto.AssignGateRequest;
import com.airport.gate.dto.GateAssignmentResponse;
import com.airport.gate.service.GateAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Gate Assignments", description = "Flight-to-gate assignment lifecycle")
public class GateAssignmentController {

    private final GateAssignmentService service;

    public GateAssignmentController(GateAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/gate-assignments")
    @Operation(summary = "Assign a gate to a flight")
    public ResponseEntity<GateAssignmentResponse> assign(@Valid @RequestBody AssignGateRequest req) {
        var created = service.assign(req);
        return ResponseEntity.created(URI.create("/api/v1/gate-assignments/" + created.getId()))
                .body(GateAssignmentResponse.from(created));
    }

    @GetMapping("/gate-assignments/{id}")
    @Operation(summary = "Retrieve a specific gate assignment")
    public GateAssignmentResponse get(@PathVariable Long id) {
        return GateAssignmentResponse.from(service.get(id));
    }

    @DeleteMapping("/gate-assignments/{id}")
    @Operation(summary = "Release a gate assignment")
    public ResponseEntity<Void> release(@PathVariable Long id) {
        service.release(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gate-assignments/flight/{flightId}")
    @Operation(summary = "Get active gate assignment for a specific flight")
    public GateAssignmentResponse getByFlight(@PathVariable Long flightId) {
        return GateAssignmentResponse.from(service.getByFlight(flightId));
    }
}
