package com.airport.security.repository;

import com.airport.security.model.SecurityClearance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SecurityClearanceRepository extends JpaRepository<SecurityClearance, Long> {
    List<SecurityClearance> findByPassengerId(Long passengerId);
    Optional<SecurityClearance> findTopByPassengerIdOrderByCreatedAtDesc(Long passengerId);
}
