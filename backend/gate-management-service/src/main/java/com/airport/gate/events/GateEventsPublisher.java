package com.airport.gate.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class GateEventsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public GateEventsPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                               @Value("${app.topics.gate-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(GateUpdatedEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.getGateId()), event);
    }
}
