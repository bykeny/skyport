package com.airport.retail.controller;

import com.airport.retail.dto.CreateProductRequest;
import com.airport.retail.dto.ProductResponse;
import com.airport.retail.service.RetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@Tag(name = "Duty-free Products", description = "Duty-free product catalog")
public class ProductController {

    private final RetailService retailService;

    public ProductController(RetailService retailService) {
        this.retailService = retailService;
    }

    @PostMapping
    @Operation(summary = "Create a duty-free product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(retailService.createProduct(request));
    }

    @GetMapping
    @Operation(summary = "List active duty-free products")
    public ResponseEntity<List<ProductResponse>> listProducts() {
        return ResponseEntity.ok(retailService.listProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get duty-free product by ID")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(retailService.getProduct(id));
    }
}
