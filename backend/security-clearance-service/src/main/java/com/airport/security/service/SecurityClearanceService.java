package com.airport.security.service;

import com.airport.security.dto.*;
import com.airport.security.events.SecurityClearedEvent;
import com.airport.security.events.SecurityEventsPublisher;
import com.airport.security.exception.NotFoundException;
import com.airport.security.model.ClearanceStatus;
import com.airport.security.model.SecurityClearance;
import com.airport.security.model.SecurityIncident;
import com.airport.security.repository.SecurityClearanceRepository;
import com.airport.security.repository.SecurityIncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecurityClearanceService {

    private final SecurityClearanceRepository clearanceRepo;
    private final SecurityIncidentRepository incidentRepo;
    private final SecurityEventsPublisher eventsPublisher;

    public SecurityClearanceService(SecurityClearanceRepository clearanceRepo,
                                     SecurityIncidentRepository incidentRepo,
                                     SecurityEventsPublisher eventsPublisher) {
        this.clearanceRepo = clearanceRepo;
        this.incidentRepo = incidentRepo;
        this.eventsPublisher = eventsPublisher;
    }

    @Transactional
    public ClearanceResponse createClearance(CreateClearanceRequest request) {
        SecurityClearance clearance = new SecurityClearance();
        clearance.setPassengerId(request.getPassengerId());
        clearance.setCheckInId(request.getCheckInId());
        clearance.setScreeningZone(request.getScreeningZone());
        clearance.setStatus(ClearanceStatus.PENDING);
        return toResponse(clearanceRepo.save(clearance));
    }

    public ClearanceResponse getClearanceById(Long id) {
        return toResponse(findById(id));
    }

    public List<ClearanceResponse> getClearancesByPassenger(Long passengerId) {
        return clearanceRepo.findByPassengerId(passengerId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ClearanceResponse updateStatus(Long id, UpdateClearanceStatusRequest request) {
        SecurityClearance clearance = findById(id);
        clearance.setStatus(request.getStatus());
        SecurityClearance saved = clearanceRepo.save(clearance);

        if (request.getStatus() == ClearanceStatus.CLEARED) {
            eventsPublisher.publishSecurityCleared(
                    new SecurityClearedEvent(saved.getId(), saved.getPassengerId(), saved.getStatus().name())
            );
        }

        return toResponse(saved);
    }

    @Transactional
    public IncidentResponse createIncident(CreateIncidentRequest request) {
        SecurityClearance clearance = findById(request.getClearanceId());
        SecurityIncident incident = new SecurityIncident();
        incident.setClearance(clearance);
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        return toIncidentResponse(incidentRepo.save(incident));
    }

    public IncidentResponse getIncidentById(Long id) {
        SecurityIncident incident = incidentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Incident not found with id: " + id));
        return toIncidentResponse(incident);
    }

    public List<IncidentResponse> getIncidentsByClearance(Long clearanceId) {
        return incidentRepo.findByClearanceId(clearanceId).stream()
                .map(this::toIncidentResponse).collect(Collectors.toList());
    }

    private SecurityClearance findById(Long id) {
        return clearanceRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Clearance not found with id: " + id));
    }

    private ClearanceResponse toResponse(SecurityClearance c) {
        ClearanceResponse r = new ClearanceResponse();
        r.setId(c.getId());
        r.setPassengerId(c.getPassengerId());
        r.setCheckInId(c.getCheckInId());
        r.setStatus(c.getStatus());
        r.setScreeningZone(c.getScreeningZone());
        r.setClearedAt(c.getClearedAt());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }

    private IncidentResponse toIncidentResponse(SecurityIncident i) {
        IncidentResponse r = new IncidentResponse();
        r.setId(i.getId());
        r.setClearanceId(i.getClearance().getId());
        r.setSeverity(i.getSeverity());
        r.setDescription(i.getDescription());
        r.setReportedAt(i.getReportedAt());
        r.setResolvedAt(i.getResolvedAt());
        return r;
    }
}