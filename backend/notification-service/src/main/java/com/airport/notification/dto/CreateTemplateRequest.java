package com.airport.notification.dto;

import com.airport.notification.domain.NotificationChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a notification template")
public class CreateTemplateRequest {

    @NotBlank(message = "Event type is required")
    @Size(max = 100, message = "Event type must be at most 100 characters")
    @Schema(example = "FLIGHT_DELAYED")
    private String eventType;

    @NotNull(message = "Channel is required")
    @Schema(example = "EMAIL")
    private NotificationChannel channel;

    @NotBlank(message = "Subject template is required")
    @Size(max = 255, message = "Subject template must be at most 255 characters")
    @Schema(example = "Flight {{flightNumber}} delayed")
    private String subjectTemplate;

    @NotBlank(message = "Body template is required")
    @Size(max = 4000, message = "Body template must be at most 4000 characters")
    @Schema(example = "Your flight {{flightNumber}} is delayed until {{departureTime}}.")
    private String bodyTemplate;

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }
    public String getSubjectTemplate() { return subjectTemplate; }
    public void setSubjectTemplate(String subjectTemplate) { this.subjectTemplate = subjectTemplate; }
    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }
}
