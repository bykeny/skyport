package com.airport.notification.service;

import com.airport.notification.domain.Notification;
import com.airport.notification.domain.NotificationStatus;
import com.airport.notification.domain.NotificationTemplate;
import com.airport.notification.dto.CreateTemplateRequest;
import com.airport.notification.dto.NotificationResponse;
import com.airport.notification.dto.SendNotificationRequest;
import com.airport.notification.dto.TemplateResponse;
import com.airport.notification.exception.NotFoundException;
import com.airport.notification.repository.NotificationRepository;
import com.airport.notification.repository.NotificationTemplateRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationTemplateRepository templateRepository) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
    }

    @Transactional
    public NotificationResponse sendNotification(SendNotificationRequest request) {
        Notification notification = new Notification();
        notification.setRecipientId(request.getRecipientId());
        notification.setRecipientType(request.getRecipientType());
        notification.setChannel(request.getChannel());
        notification.setSubject(request.getSubject());
        notification.setBody(request.getBody());
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());

        return toNotificationResponse(notificationRepository.save(notification));
    }

    public NotificationResponse getNotification(Long id) {
        return toNotificationResponse(findNotification(id));
    }

    public List<NotificationResponse> getNotificationsByRecipient(Long recipientId) {
        return notificationRepository.findByRecipientId(recipientId).stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TemplateResponse createTemplate(CreateTemplateRequest request) {
        NotificationTemplate template = new NotificationTemplate();
        template.setEventType(request.getEventType());
        template.setChannel(request.getChannel());
        template.setSubjectTemplate(request.getSubjectTemplate());
        template.setBodyTemplate(request.getBodyTemplate());

        return toTemplateResponse(templateRepository.save(template));
    }

    public List<TemplateResponse> getTemplatesByEventType(String eventType) {
        List<NotificationTemplate> templates = templateRepository.findByEventType(eventType);
        if (templates.isEmpty()) {
            throw new NotFoundException("Notification template not found for event type: " + eventType);
        }
        return templates.stream()
                .map(this::toTemplateResponse)
                .collect(Collectors.toList());
    }

    private Notification findNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id: " + id));
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setRecipientId(notification.getRecipientId());
        response.setRecipientType(notification.getRecipientType());
        response.setChannel(notification.getChannel());
        response.setSubject(notification.getSubject());
        response.setBody(notification.getBody());
        response.setStatus(notification.getStatus());
        response.setSentAt(notification.getSentAt());
        return response;
    }

    private TemplateResponse toTemplateResponse(NotificationTemplate template) {
        TemplateResponse response = new TemplateResponse();
        response.setId(template.getId());
        response.setEventType(template.getEventType());
        response.setChannel(template.getChannel());
        response.setSubjectTemplate(template.getSubjectTemplate());
        response.setBodyTemplate(template.getBodyTemplate());
        return response;
    }
}
