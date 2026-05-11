package com.airport.retail.repository;

import com.airport.retail.domain.DutyFreeOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DutyFreeOrderRepository extends JpaRepository<DutyFreeOrder, Long> {
    List<DutyFreeOrder> findByPassengerIdOrderByCreatedAtDesc(Long passengerId);
    List<DutyFreeOrder> findByFlightIdOrderByCreatedAtDesc(Long flightId);
}
