package com.airport.gate.service;

import com.airport.gate.dto.AssignGateRequest;
import com.airport.gate.events.GateEventsPublisher;
import com.airport.gate.events.GateUpdatedEvent;
import com.airport.gate.exception.NotFoundException;
import com.airport.gate.model.Gate;
import com.airport.gate.model.GateAssignment;
import com.airport.gate.model.GateStatus;
import com.airport.gate.repository.GateAssignmentRepository;
import com.airport.gate.repository.GateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GateAssignmentService {

    private final GateRepository gateRepository;
    private final GateAssignmentRepository assignmentRepository;
    private final GateEventsPublisher publisher;

    public GateAssignmentService(GateRepository gateRepository,
                                 GateAssignmentRepository assignmentRepository,
                                 GateEventsPublisher publisher) {
        this.gateRepository = gateRepository;
        this.assignmentRepository = assignmentRepository;
        this.publisher = publisher;
    }

    @Transactional
    public GateAssignment assign(AssignGateRequest req) {
        Gate gate = gateRepository.findById(req.getGateId())
                .orElseThrow(() -> new NotFoundException("Gate %d not found".formatted(req.getGateId())));
        if (gate.getStatus() == GateStatus.MAINTENANCE) {
            throw new IllegalStateException("Cannot assign a gate that is under maintenance");
        }
        if (gate.getStatus() == GateStatus.OCCUPIED) {
            throw new IllegalStateException("Gate is already occupied");
        }

        GateAssignment assignment = new GateAssignment();
        assignment.setGate(gate);
        assignment.setFlightId(req.getFlightId());
        if (req.getAssignedAt() != null) {
            assignment.setAssignedAt(req.getAssignedAt());
        }
        gate.setStatus(GateStatus.OCCUPIED);
        gateRepository.save(gate);

        GateAssignment saved = assignmentRepository.save(assignment);
        publisher.publish(new GateUpdatedEvent(gate.getId(), gate.getGateNumber(), gate.getStatus(), req.getFlightId()));
        return saved;
    }

    @Transactional(readOnly = true)
    public GateAssignment get(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GateAssignment %d not found".formatted(id)));
    }

    @Transactional
    public void release(Long id) {
        GateAssignment a = get(id);
        if (a.getReleasedAt() != null) {
            throw new IllegalStateException("Gate assignment has already been released");
        }
        a.releaseNow();
        Gate gate = a.getGate();
        gate.setStatus(GateStatus.AVAILABLE);
        assignmentRepository.save(a);
        gateRepository.save(gate);
        publisher.publish(new GateUpdatedEvent(gate.getId(), gate.getGateNumber(), gate.getStatus(), a.getFlightId()));
    }

    @Transactional(readOnly = true)
    public GateAssignment getByFlight(Long flightId) {
        return assignmentRepository.findFirstByFlightIdAndReleasedAtIsNull(flightId)
                .orElseThrow(() -> new NotFoundException("Active assignment for flight %d not found".formatted(flightId)));
    }
}
