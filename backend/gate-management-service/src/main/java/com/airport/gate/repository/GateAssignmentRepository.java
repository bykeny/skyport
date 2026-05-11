package com.airport.gate.repository;

import com.airport.gate.model.GateAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GateAssignmentRepository extends JpaRepository<GateAssignment, Long> {
    Optional<GateAssignment> findFirstByFlightIdAndReleasedAtIsNull(Long flightId);
}
