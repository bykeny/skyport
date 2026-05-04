package com.airport.baggage.repository;

import com.airport.baggage.repository.*;
import com.airport.baggage.model.CarouselAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarouselAssignmentRepository extends JpaRepository<CarouselAssignment, Long> {
    Optional<CarouselAssignment> findByBaggageItemId(Long baggageItemId);
}
