package com.airport.security.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventsPublisher {

    private static final Logger log = LoggerFactory.getLogger(SecurityEventsPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topics.security-events}")
    private String securityTopic;

    public SecurityEventsPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishSecurityCleared(SecurityClearedEvent event) {
        log.info("Publishing SecurityCleared event for passengerId={}", event.getPassengerId());
        kafkaTemplate.send(securityTopic, String.valueOf(event.getClearanceId()), event);
    }
}
