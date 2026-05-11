package com.airport.flight.service;

import com.airport.flight.dto.CreateFlightRequest;
import com.airport.flight.dto.UpdateFlightRequest;
import com.airport.flight.dto.UpdateStatusRequest;
import com.airport.flight.events.FlightEventsPublisher;
import com.airport.flight.events.FlightStatusChangedEvent;
import com.airport.flight.exception.NotFoundException;
import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;
import com.airport.flight.repository.FlightRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightService {

    private final FlightRepository repository;
    private final FlightEventsPublisher publisher;

    public FlightService(FlightRepository repository, FlightEventsPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public Flight create(CreateFlightRequest req) {
        Flight f = new Flight();
        f.setFlightNumber(req.getFlightNumber());
        f.setOrigin(req.getOrigin());
        f.setDestination(req.getDestination());
        f.setScheduledDeparture(req.getScheduledDeparture());
        f.setScheduledArrival(req.getScheduledArrival());
        f.setAircraftType(req.getAircraftType());
        f.setAirlineCode(req.getAirlineCode());
        return repository.save(f);
    }

    @Transactional(readOnly = true)
    public Flight get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(id)));
    }

    @Transactional
    public Flight update(Long id, UpdateFlightRequest req) {
        Flight f = get(id);
        f.setScheduledDeparture(req.getScheduledDeparture());
        f.setScheduledArrival(req.getScheduledArrival());
        f.setAircraftType(req.getAircraftType());
        f.setAirlineCode(req.getAirlineCode());
        return repository.save(f);
    }

    @Transactional
    public Flight updateStatus(Long id, UpdateStatusRequest req) {
        Flight f = get(id);
        if (f.getStatus() == FlightStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status of a cancelled flight");
        }
        f.setStatus(req.getStatus());
        if (req.getActualDeparture() != null) {
            f.setActualDeparture(req.getActualDeparture());
        }
        Flight saved = repository.save(f);
        publisher.publishStatusChanged(new FlightStatusChangedEvent(
                saved.getId(), saved.getFlightNumber(), saved.getStatus(),
                saved.getScheduledDeparture(), saved.getActualDeparture()
        ));
        return saved;
    }

    @Transactional
    public Flight cancel(Long id) {
        UpdateStatusRequest req = new UpdateStatusRequest();
        req.setStatus(FlightStatus.CANCELLED);
        Flight f = get(id);
        f.setStatus(FlightStatus.CANCELLED);
        Flight saved = repository.save(f);
        publisher.publishStatusChanged(new FlightStatusChangedEvent(
                saved.getId(), saved.getFlightNumber(), saved.getStatus(),
                saved.getScheduledDeparture(), saved.getActualDeparture()
        ));
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Flight> list(FlightStatus status, LocalDateTime from, LocalDateTime to) {
        return repository.findByFilters(status, from, to);
    }

    @Transactional(readOnly = true)
    public List<Flight> search(String origin, String destination, LocalDate day) {
        LocalDateTime start = day != null ? day.atStartOfDay() : null;
        LocalDateTime end = day != null ? day.atTime(LocalTime.MAX) : null;
        return repository.search(origin, destination, start, end);
    }
}
