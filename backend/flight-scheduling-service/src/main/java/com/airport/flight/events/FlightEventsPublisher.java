package com.airport.flight.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FlightEventsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public FlightEventsPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                 @Value("${app.topics.flight-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publishStatusChanged(FlightStatusChangedEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.getFlightId()), event);
    }
}
