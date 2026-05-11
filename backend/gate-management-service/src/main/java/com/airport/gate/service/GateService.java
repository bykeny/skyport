package com.airport.gate.service;

import com.airport.gate.events.GateEventsPublisher;
import com.airport.gate.events.GateUpdatedEvent;
import com.airport.gate.exception.NotFoundException;
import com.airport.gate.model.Gate;
import com.airport.gate.model.GateStatus;
import com.airport.gate.repository.GateRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GateService {

    private final GateRepository repository;
    private final GateEventsPublisher publisher;

    public GateService(GateRepository repository, GateEventsPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional(readOnly = true)
    public List<Gate> list() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Gate get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gate %d not found".formatted(id)));
    }

    @Transactional
    public Gate updateStatus(Long id, GateStatus status) {
        Gate g = get(id);
        g.setStatus(status);
        Gate saved = repository.save(g);
        publisher.publish(new GateUpdatedEvent(saved.getId(), saved.getGateNumber(), saved.getStatus(), null));
        return saved;
    }
}
