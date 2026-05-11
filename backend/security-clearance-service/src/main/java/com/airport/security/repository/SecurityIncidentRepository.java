package com.airport.security.repository;

import com.airport.security.model.SecurityIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecurityIncidentRepository extends JpaRepository<SecurityIncident, Long> {
    List<SecurityIncident> findByClearanceId(Long clearanceId);
}
