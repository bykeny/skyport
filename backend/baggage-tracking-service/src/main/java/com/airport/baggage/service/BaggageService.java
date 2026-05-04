package com.airport.baggage.service;

import com.airport.baggage.dto.*;
import com.airport.baggage.events.BaggageEventsPublisher;
import com.airport.baggage.events.BaggageRegisteredEvent;
import com.airport.baggage.events.BaggageStatusChangedEvent;
import com.airport.baggage.exception.NotFoundException;
import com.airport.baggage.model.BaggageItem;
import com.airport.baggage.model.BaggageStatus;
import com.airport.baggage.model.CarouselAssignment;
import com.airport.baggage.repository.BaggageItemRepository;
import com.airport.baggage.repository.CarouselAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BaggageService {

    private final BaggageItemRepository baggageRepo;
    private final CarouselAssignmentRepository carouselRepo;
    private final BaggageEventsPublisher eventsPublisher;

    public BaggageService(BaggageItemRepository baggageRepo,
                          CarouselAssignmentRepository carouselRepo,
                          BaggageEventsPublisher eventsPublisher) {
        this.baggageRepo = baggageRepo;
        this.carouselRepo = carouselRepo;
        this.eventsPublisher = eventsPublisher;
    }

    @Transactional
    public BaggageResponse registerBaggage(RegisterBaggageRequest request) {
        BaggageItem item = new BaggageItem();
        item.setPassengerId(request.getPassengerId());
        item.setFlightId(request.getFlightId());
        item.setTagNumber("TAG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        item.setBarcode("BAR-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        item.setWeightValue(request.getWeightValue());
        item.setWeightUnit(request.getWeightUnit() != null ? request.getWeightUnit() : "kg");
        item.setStatus(BaggageStatus.REGISTERED);

        BaggageItem saved = baggageRepo.save(item);

        eventsPublisher.publishBaggageRegistered(
                new BaggageRegisteredEvent(saved.getId(), saved.getPassengerId(), saved.getFlightId(), saved.getTagNumber())
        );

        return toResponse(saved);
    }

    public BaggageResponse getBaggageById(Long id) {
        return toResponse(findById(id));
    }

    public BaggageResponse getBaggageByTag(String tagNumber) {
        BaggageItem item = baggageRepo.findByTagNumber(tagNumber)
                .orElseThrow(() -> new NotFoundException("Baggage not found with tag: " + tagNumber));
        return toResponse(item);
    }

    public List<BaggageResponse> getBaggageByPassenger(Long passengerId) {
        return baggageRepo.findByPassengerId(passengerId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public List<BaggageResponse> getBaggageByFlight(Long flightId) {
        return baggageRepo.findByFlightId(flightId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public BaggageResponse updateStatus(Long id, UpdateBaggageStatusRequest request) {
        BaggageItem item = findById(id);
        item.setStatus(request.getStatus());
        BaggageItem saved = baggageRepo.save(item);

        eventsPublisher.publishBaggageStatusChanged(
                new BaggageStatusChangedEvent(saved.getId(), saved.getPassengerId(), saved.getStatus(), saved.getTagNumber())
        );

        return toResponse(saved);
    }

    @Transactional
    public CarouselAssignmentResponse assignCarousel(Long baggageId, CarouselAssignmentRequest request) {
        BaggageItem item = findById(baggageId);

        if (carouselRepo.findByBaggageItemId(baggageId).isPresent()) {
            throw new IllegalStateException("Baggage already assigned to a carousel");
        }

        CarouselAssignment assignment = new CarouselAssignment();
        assignment.setBaggageItem(item);
        assignment.setCarouselNumber(request.getCarouselNumber());
        CarouselAssignment saved = carouselRepo.save(assignment);

        item.setStatus(BaggageStatus.ARRIVED);
        baggageRepo.save(item);

        eventsPublisher.publishBaggageStatusChanged(
                new BaggageStatusChangedEvent(item.getId(), item.getPassengerId(), BaggageStatus.ARRIVED, item.getTagNumber())
        );

        return toCarouselResponse(saved);
    }

    private BaggageItem findById(Long id) {
        return baggageRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Baggage not found with id: " + id));
    }

    private BaggageResponse toResponse(BaggageItem item) {
        BaggageResponse r = new BaggageResponse();
        r.setId(item.getId());
        r.setPassengerId(item.getPassengerId());
        r.setFlightId(item.getFlightId());
        r.setTagNumber(item.getTagNumber());
        r.setBarcode(item.getBarcode());
        r.setIssuedAt(item.getIssuedAt());
        r.setWeightValue(item.getWeightValue());
        r.setWeightUnit(item.getWeightUnit());
        r.setStatus(item.getStatus());
        r.setCreatedAt(item.getCreatedAt());
        r.setUpdatedAt(item.getUpdatedAt());
        return r;
    }

    private CarouselAssignmentResponse toCarouselResponse(CarouselAssignment ca) {
        CarouselAssignmentResponse r = new CarouselAssignmentResponse();
        r.setId(ca.getId());
        r.setBaggageItemId(ca.getBaggageItem().getId());
        r.setCarouselNumber(ca.getCarouselNumber());
        r.setAssignedAt(ca.getAssignedAt());
        r.setArrivedAt(ca.getArrivedAt());
        return r;
    }
}