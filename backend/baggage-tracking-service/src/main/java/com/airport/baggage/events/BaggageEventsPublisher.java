package com.airport.baggage.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BaggageEventsPublisher {

    private static final Logger log = LoggerFactory.getLogger(BaggageEventsPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topics.baggage-events}")
    private String baggageTopic;

    public BaggageEventsPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBaggageRegistered(BaggageRegisteredEvent event) {
        log.info("Publishing BaggageRegistered event for baggageId={}", event.getBaggageId());
        kafkaTemplate.send(baggageTopic, String.valueOf(event.getBaggageId()), event);
    }

    public void publishBaggageStatusChanged(BaggageStatusChangedEvent event) {
        log.info("Publishing BaggageStatusChanged event for baggageId={}", event.getBaggageId());
        kafkaTemplate.send(baggageTopic, String.valueOf(event.getBaggageId()), event);
    }
}
