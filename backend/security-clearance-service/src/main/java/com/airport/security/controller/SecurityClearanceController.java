package com.airport.security.controller;

import com.airport.security.dto.*;
import com.airport.security.service.SecurityClearanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Security Clearance", description = "Passenger security screening management")
public class SecurityClearanceController {

    private final SecurityClearanceService service;

    public SecurityClearanceController(SecurityClearanceService service) {
        this.service = service;
    }

    @PostMapping("/clearances")
    @Operation(summary = "Create a security clearance record")
    public ResponseEntity<ClearanceResponse> createClearance(@Valid @RequestBody CreateClearanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createClearance(request));
    }

    @GetMapping("/clearances/{id}")
    @Operation(summary = "Get clearance by ID")
    public ResponseEntity<ClearanceResponse> getClearance(@PathVariable Long id) {
        return ResponseEntity.ok(service.getClearanceById(id));
    }

    @PatchMapping("/clearances/{id}/status")
    @Operation(summary = "Update clearance status")
    public ResponseEntity<ClearanceResponse> updateStatus(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateClearanceStatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request));
    }

    @GetMapping("/clearances/passenger/{passengerId}")
    @Operation(summary = "Get clearances by passenger")
    public ResponseEntity<List<ClearanceResponse>> getClearancesByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(service.getClearancesByPassenger(passengerId));
    }

    @PostMapping("/incidents")
    @Operation(summary = "Report a security incident")
    public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createIncident(request));
    }

    @GetMapping("/incidents/{id}")
    @Operation(summary = "Get incident by ID")
    public ResponseEntity<IncidentResponse> getIncident(@PathVariable Long id) {
        return ResponseEntity.ok(service.getIncidentById(id));
    }

    @GetMapping("/incidents/clearance/{clearanceId}")
    @Operation(summary = "Get all incidents for a clearance")
    public ResponseEntity<List<IncidentResponse>> getIncidentsByClearance(@PathVariable Long clearanceId) {
        return ResponseEntity.ok(service.getIncidentsByClearance(clearanceId));
    }
}