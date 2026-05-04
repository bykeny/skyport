package com.airport.notification.repository;

import com.airport.notification.domain.NotificationChannel;
import com.airport.notification.domain.NotificationTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    List<NotificationTemplate> findByEventType(String eventType);
    Optional<NotificationTemplate> findByEventTypeAndChannel(String eventType, NotificationChannel channel);
}
