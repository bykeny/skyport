package com.airport.gate.repository;

import com.airport.gate.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GateRepository extends JpaRepository<Gate, Long> {
    Optional<Gate> findByGateNumber(String gateNumber);
}
