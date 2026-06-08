package com.airport.checkin.service;

import com.airport.checkin.dto.BaggageDropRequest;
import com.airport.checkin.dto.CheckInResponse;
import com.airport.checkin.dto.CreateCheckInRequest;
import com.airport.checkin.dto.UpdateCheckInStatusRequest;
import com.airport.checkin.events.CheckInCompletedEvent;
import com.airport.checkin.events.CheckInEventsPublisher;
import com.airport.checkin.events.CheckInStatusChangedEvent;
import com.airport.checkin.exception.NotFoundException;
import com.airport.checkin.integration.FlightValidationClient;
import com.airport.checkin.model.CheckInStatus;
import com.airport.checkin.model.PassengerCheckIn;
import com.airport.checkin.repository.PassengerCheckInRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckInService {

    private final PassengerCheckInRepository checkInRepository;
    private final FlightValidationClient flightValidationClient;
    private final CheckInEventsPublisher eventsPublisher;

    public CheckInService(PassengerCheckInRepository checkInRepository,
                          FlightValidationClient flightValidationClient,
                          CheckInEventsPublisher eventsPublisher) {
        this.checkInRepository = checkInRepository;
        this.flightValidationClient = flightValidationClient;
        this.eventsPublisher = eventsPublisher;
    }

    @Transactional
    public CheckInResponse createCheckIn(CreateCheckInRequest request) {
        flightValidationClient.validateFlightExists(request.getFlightId());
        if (checkInRepository.existsByPassengerIdAndFlightId(request.getPassengerId(), request.getFlightId())) {
            throw new IllegalStateException("Passenger is already checked in for this flight");
        }

        PassengerCheckIn checkIn = new PassengerCheckIn();
        checkIn.setPassengerId(request.getPassengerId());
        checkIn.setFlightId(request.getFlightId());
        checkIn.setSeatNumber(request.getSeatNumber());
        checkIn.setBaggageCount(request.getBaggageCount() == null ? 0 : request.getBaggageCount());
        checkIn.setBoardingPassCode("BP-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        checkIn.setStatus(CheckInStatus.CHECKED_IN);

        PassengerCheckIn saved = checkInRepository.save(checkIn);
        eventsPublisher.publishCheckInCompleted(new CheckInCompletedEvent(
                saved.getId(),
                saved.getPassengerId(),
                saved.getFlightId(),
                saved.getBoardingPassCode(),
                saved.getBaggageCount()
        ));

        return toResponse(saved);
    }

    public CheckInResponse getCheckIn(Long id) {
        return toResponse(findById(id));
    }

    public CheckInResponse getPassengerFlightCheckIn(Long passengerId, Long flightId) {
        return checkInRepository.findByPassengerIdAndFlightId(passengerId, flightId)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Check-in not found for passenger " + passengerId + " and flight " + flightId));
    }

    public List<CheckInResponse> getPassengerCheckIns(Long passengerId) {
        return checkInRepository.findByPassengerId(passengerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CheckInResponse> getFlightCheckIns(Long flightId) {
        return checkInRepository.findByFlightId(flightId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CheckInResponse updateStatus(Long id, UpdateCheckInStatusRequest request) {
        PassengerCheckIn checkIn = findById(id);
        ensureNotCancelled(checkIn);
        checkIn.setStatus(request.getStatus());
        PassengerCheckIn saved = checkInRepository.save(checkIn);
        eventsPublisher.publishStatusChanged(new CheckInStatusChangedEvent(
                saved.getId(),
                saved.getPassengerId(),
                saved.getFlightId(),
                saved.getStatus()
        ));
        return toResponse(saved);
    }

    @Transactional
    public CheckInResponse recordBaggageDrop(Long id, BaggageDropRequest request) {
        PassengerCheckIn checkIn = findById(id);
        ensureNotCancelled(checkIn);
        checkIn.setBaggageCount(request.getBaggageCount());
        checkIn.setStatus(CheckInStatus.BAGGAGE_DROPPED);
        PassengerCheckIn saved = checkInRepository.save(checkIn);
        eventsPublisher.publishStatusChanged(new CheckInStatusChangedEvent(
                saved.getId(),
                saved.getPassengerId(),
                saved.getFlightId(),
                saved.getStatus()
        ));
        return toResponse(saved);
    }

    @Transactional
    public CheckInResponse cancelCheckIn(Long id) {
        PassengerCheckIn checkIn = findById(id);
        checkIn.setStatus(CheckInStatus.CANCELLED);
        PassengerCheckIn saved = checkInRepository.save(checkIn);
        eventsPublisher.publishStatusChanged(new CheckInStatusChangedEvent(
                saved.getId(),
                saved.getPassengerId(),
                saved.getFlightId(),
                saved.getStatus()
        ));
        return toResponse(saved);
    }

    private PassengerCheckIn findById(Long id) {
        return checkInRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Check-in not found with id: " + id));
    }

    private void ensureNotCancelled(PassengerCheckIn checkIn) {
        if (checkIn.getStatus() == CheckInStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled check-in cannot be changed");
        }
    }

    private CheckInResponse toResponse(PassengerCheckIn checkIn) {
        CheckInResponse response = new CheckInResponse();
        response.setId(checkIn.getId());
        response.setPassengerId(checkIn.getPassengerId());
        response.setFlightId(checkIn.getFlightId());
        response.setSeatNumber(checkIn.getSeatNumber());
        response.setBoardingPassCode(checkIn.getBoardingPassCode());
        response.setBaggageCount(checkIn.getBaggageCount());
        response.setStatus(checkIn.getStatus());
        response.setCheckedInAt(checkIn.getCheckedInAt());
        response.setCreatedAt(checkIn.getCreatedAt());
        response.setUpdatedAt(checkIn.getUpdatedAt());
        return response;
    }
}
