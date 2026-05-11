package com.airport.gate.controller;

import com.airport.gate.dto.GateResponse;
import com.airport.gate.dto.UpdateGateStatusRequest;
import com.airport.gate.service.GateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Gate Management", description = "Gate catalog and availability management")
public class GateController {

    private final GateService service;

    public GateController(GateService service) {
        this.service = service;
    }

    @GetMapping("/gates")
    @Operation(summary = "List all gates with their current availability status")
    public List<GateResponse> list() {
        return service.list().stream().map(GateResponse::from).toList();
    }

    @GetMapping("/gates/{id}")
    @Operation(summary = "Retrieve details of a specific gate")
    public GateResponse get(@PathVariable Long id) {
        return GateResponse.from(service.get(id));
    }

    @PatchMapping("/gates/{id}/status")
    @Operation(summary = "Update gate status (AVAILABLE, OCCUPIED, MAINTENANCE)")
    public ResponseEntity<GateResponse> updateStatus(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateGateStatusRequest req) {
        return ResponseEntity.ok(GateResponse.from(service.updateStatus(id, req.getStatus())));
    }
}
