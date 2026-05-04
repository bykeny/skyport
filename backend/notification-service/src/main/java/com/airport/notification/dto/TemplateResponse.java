package com.airport.notification.dto;

import com.airport.notification.domain.NotificationChannel;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Notification template response")
public class TemplateResponse {

    private Long id;
    private String eventType;
    private NotificationChannel channel;
    private String subjectTemplate;
    private String bodyTemplate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }
    public String getSubjectTemplate() { return subjectTemplate; }
    public void setSubjectTemplate(String subjectTemplate) { this.subjectTemplate = subjectTemplate; }
    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }
}
