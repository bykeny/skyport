package com.airport.gate.events;

import com.airport.gate.model.Gate;
import com.airport.gate.model.GateStatus;
import com.airport.gate.repository.GateAssignmentRepository;
import com.airport.gate.repository.GateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FlightEventsListener {

    private static final Logger log = LoggerFactory.getLogger(FlightEventsListener.class);

    private final GateAssignmentRepository assignmentRepository;
    private final GateRepository gateRepository;

    public FlightEventsListener(GateAssignmentRepository assignmentRepository,
                                GateRepository gateRepository) {
        this.assignmentRepository = assignmentRepository;
        this.gateRepository = gateRepository;
    }

    @Transactional
    @KafkaListener(topics = "${app.topics.flight-events}", groupId = "gate-mgmt-service")
    public void onFlightStatusChanged(FlightStatusChangedEvent event) {
        String status = event.getNewStatus() != null ? event.getNewStatus() : "";
        if (status.equalsIgnoreCase("ARRIVED") || status.equalsIgnoreCase("CANCELLED")) {
            assignmentRepository.findFirstByFlightIdAndReleasedAtIsNull(event.getFlightId())
                    .ifPresent(assignment -> {
                        assignment.releaseNow();
                        Gate gate = assignment.getGate();
                        gate.setStatus(GateStatus.AVAILABLE);
                        assignmentRepository.save(assignment);
                        gateRepository.save(gate);
                        log.info("Auto-released gate {} for flight {}", gate.getGateNumber(), event.getFlightNumber());
                    });
        }
    }
}
