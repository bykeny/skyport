package com.airport.notification.dto;

import com.airport.notification.domain.NotificationChannel;
import com.airport.notification.domain.RecipientType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for sending a notification")
public class SendNotificationRequest {

    @NotNull(message = "Recipient ID is required")
    @Schema(example = "42")
    private Long recipientId;

    @NotNull(message = "Recipient type is required")
    @Schema(example = "PASSENGER")
    private RecipientType recipientType;

    @NotNull(message = "Channel is required")
    @Schema(example = "EMAIL")
    private NotificationChannel channel;

    @NotBlank(message = "Subject is required")
    @Size(max = 255, message = "Subject must be at most 255 characters")
    @Schema(example = "Gate changed")
    private String subject;

    @NotBlank(message = "Body is required")
    @Size(max = 4000, message = "Body must be at most 4000 characters")
    @Schema(example = "Your flight gate has changed to A12.")
    private String body;

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public RecipientType getRecipientType() { return recipientType; }
    public void setRecipientType(RecipientType recipientType) { this.recipientType = recipientType; }
    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
