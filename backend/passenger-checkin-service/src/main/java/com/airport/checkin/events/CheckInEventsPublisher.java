package com.airport.checkin.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CheckInEventsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String checkInEventsTopic;

    public CheckInEventsPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                  @Value("${app.topics.checkin-events}") String checkInEventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.checkInEventsTopic = checkInEventsTopic;
    }

    public void publishCheckInCompleted(CheckInCompletedEvent event) {
        kafkaTemplate.send(checkInEventsTopic, String.valueOf(event.getCheckInId()), event);
    }

    public void publishStatusChanged(CheckInStatusChangedEvent event) {
        kafkaTemplate.send(checkInEventsTopic, String.valueOf(event.getCheckInId()), event);
    }
}
