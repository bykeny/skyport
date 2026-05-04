package com.airport.gate.controller;
package com.airport.gate.controller;

import com.airport.gate.dto.AssignGateRequest;
import com.airport.gate.dto.GateAssignmentResponse;
import com.airport.gate.model.GateAssignment;
import com.airport.gate.service.GateAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class GateAssignmentController {

    private final GateAssignmentService service;

    public GateAssignmentController(GateAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/gate-assignments")
    public ResponseEntity<GateAssignmentResponse> assign(@Valid @RequestBody AssignGateRequest req) {
        GateAssignment created = service.assign(req);
        return ResponseEntity.created(URI.create("/api/v1/gate-assignments/" + created.getId()))
                .body(GateAssignmentResponse.from(created));
    }

    @GetMapping("/gate-assignments/{id}")
    public GateAssignmentResponse get(@PathVariable Long id) {
        return GateAssignmentResponse.from(service.get(id));
    }

    @DeleteMapping("/gate-assignments/{id}")
    public ResponseEntity<Void> release(@PathVariable Long id) {
        service.release(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gate-assignments/flight/{flightId}")
    public GateAssignmentResponse getByFlight(@PathVariable Long flightId) {
        return GateAssignmentResponse.from(service.getByFlight(flightId));
    }
}
import com.airport.gate.dto.AssignGateRequest;
import com.airport.gate.dto.GateAssignmentResponse;
import com.airport.gate.model.GateAssignment;
import com.airport.gate.service.GateAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
public class GateAssignmentController {

    private final GateAssignmentService service;

    public GateAssignmentController(GateAssignmentService service) {
        this.service = service;
    }

    @PostMapping("/gate-assignments")
    public ResponseEntity<GateAssignmentResponse> assign(@Valid @RequestBody AssignGateRequest req) {
        GateAssignment created = service.assign(req);
        return ResponseEntity.created(URI.create("/api/v1/gate-assignments/" + created.getId()))
                .body(GateAssignmentResponse.from(created));
    }

    @GetMapping("/gate-assignments/{id}")
    public GateAssignmentResponse get(@PathVariable Long id) {
        return GateAssignmentResponse.from(service.get(id));
    }

    @DeleteMapping("/gate-assignments/{id}")
    public ResponseEntity<Void> release(@PathVariable Long id) {
        service.release(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gate-assignments/flight/{flightId}")
    public GateAssignmentResponse getByFlight(@PathVariable Long flightId) {
        return GateAssignmentResponse.from(service.getByFlight(flightId));
    }
}
