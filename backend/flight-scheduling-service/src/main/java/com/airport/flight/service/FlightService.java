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
import org.springframework.data.domain.Sort;
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
        f.setFlightNumber(req.getFlightNumber().trim().toUpperCase());
        f.setOrigin(req.getOrigin().trim().toUpperCase());
        f.setDestination(req.getDestination().trim().toUpperCase());
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
        return repository.findAll(Sort.by(Sort.Direction.ASC, "scheduledDeparture")).stream()
                .filter(f -> status == null || f.getStatus() == status)
                .filter(f -> from == null || !f.getScheduledDeparture().isBefore(from))
                .filter(f -> to == null || !f.getScheduledDeparture().isAfter(to))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Flight> search(String origin, String destination, LocalDate day) {
        LocalDateTime start = day != null ? day.atStartOfDay() : null;
        LocalDateTime end = day != null ? day.atTime(LocalTime.MAX) : null;
        return repository.findAll(Sort.by(Sort.Direction.ASC, "scheduledDeparture")).stream()
                .filter(f -> origin == null || f.getOrigin().equalsIgnoreCase(origin))
                .filter(f -> destination == null || f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> start == null || !f.getScheduledDeparture().isBefore(start))
                .filter(f -> end == null || !f.getScheduledDeparture().isAfter(end))
                .toList();
    }
}
