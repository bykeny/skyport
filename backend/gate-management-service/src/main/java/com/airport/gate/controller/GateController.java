package com.airport.gate.controller;

import com.airport.gate.dto.GateResponse;
import com.airport.gate.model.Gate;
import com.airport.gate.model.GateStatus;
import com.airport.gate.service.GateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GateController {

    private final GateService service;
package com.airport.gate.controller;

import com.airport.gate.dto.GateResponse;
import com.airport.gate.model.Gate;
import com.airport.gate.model.GateStatus;
import com.airport.gate.service.GateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GateController {

    private final GateService service;

    public GateController(GateService service) {
        this.service = service;
    }

    @GetMapping("/gates")
    public List<GateResponse> list() {
        List<Gate> gates = service.list();
        return gates.stream().map(GateResponse::from).toList();
    }

    @GetMapping("/gates/{id}")
    public GateResponse get(@PathVariable Long id) {
        return GateResponse.from(service.get(id));
    }

    @PatchMapping("/gates/{id}/status")
    public GateResponse updateStatus(@PathVariable Long id, @RequestParam GateStatus status) {
        return GateResponse.from(service.updateStatus(id, status));
    }
}
    public GateController(GateService service) {
        this.service = service;
    }

    @GetMapping("/gates")
    public List<GateResponse> list() {
        List<Gate> gates = service.list();
        return gates.stream().map(GateResponse::from).toList();
    }

    @GetMapping("/gates/{id}")
    public GateResponse get(@PathVariable Long id) {
        return GateResponse.from(service.get(id));
    }

    @PatchMapping("/gates/{id}/status")
    public GateResponse updateStatus(@PathVariable Long id, @RequestParam GateStatus status) {
        return GateResponse.from(service.updateStatus(id, status));
    }
}
