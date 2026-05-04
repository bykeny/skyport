package com.airport.notification.controller;

import com.airport.notification.dto.CreateTemplateRequest;
import com.airport.notification.dto.TemplateResponse;
import com.airport.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/templates")
@Tag(name = "Notification Templates", description = "Notification template management")
public class TemplateController {

    private final NotificationService notificationService;

    public TemplateController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @Operation(summary = "Create a notification template")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Template created",
                    content = @Content(schema = @Schema(implementation = TemplateResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<TemplateResponse> createTemplate(@Valid @RequestBody CreateTemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createTemplate(request));
    }

    @GetMapping("/event/{eventType}")
    @Operation(summary = "Get templates for an event type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Templates found"),
            @ApiResponse(responseCode = "404", description = "Template not found",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<List<TemplateResponse>> getTemplatesByEventType(@PathVariable String eventType) {
        return ResponseEntity.ok(notificationService.getTemplatesByEventType(eventType));
    }
}
