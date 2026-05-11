package com.airport.retail.controller;

import com.airport.retail.dto.CreateOrderRequest;
import com.airport.retail.dto.OrderResponse;
import com.airport.retail.dto.UpdateOrderStatusRequest;
import com.airport.retail.service.RetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@Tag(name = "Duty-free Orders", description = "Duty-free pre-order lifecycle")
public class OrderController {

    private final RetailService retailService;

    public OrderController(RetailService retailService) {
        this.retailService = retailService;
    }

    @PostMapping
    @Operation(summary = "Create a duty-free order for a checked-in passenger")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(retailService.createOrder(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get duty-free order by ID")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(retailService.getOrder(id));
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "List duty-free orders by passenger")
    public ResponseEntity<List<OrderResponse>> getPassengerOrders(@PathVariable Long passengerId) {
        return ResponseEntity.ok(retailService.getPassengerOrders(passengerId));
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "List duty-free orders by flight")
    public ResponseEntity<List<OrderResponse>> getFlightOrders(@PathVariable Long flightId) {
        return ResponseEntity.ok(retailService.getFlightOrders(flightId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update duty-free order status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(retailService.updateOrderStatus(id, request));
    }
}
