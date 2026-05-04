package com.airport.baggage.repository;

import com.airport.baggage.model.BaggageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BaggageItemRepository extends JpaRepository<BaggageItem, Long> {
    List<BaggageItem> findByPassengerId(Long passengerId);
    List<BaggageItem> findByFlightId(Long flightId);
    Optional<BaggageItem> findByTagNumber(String tagNumber);
}
