package com.airport.baggage.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CheckInEventsListener {

    private static final Logger log = LoggerFactory.getLogger(CheckInEventsListener.class);

    @KafkaListener(topics = "${app.topics.checkin-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void onCheckInCompleted(CheckInCompletedEvent event) {
        log.info("Received CheckInCompleted event for passengerId={}, flightId={}",
                event.getPassengerId(), event.getFlightId());
    }
}
