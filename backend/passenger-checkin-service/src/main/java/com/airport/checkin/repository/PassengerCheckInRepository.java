package com.airport.checkin.repository;

import com.airport.checkin.model.PassengerCheckIn;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerCheckInRepository extends JpaRepository<PassengerCheckIn, Long> {

    Optional<PassengerCheckIn> findByPassengerIdAndFlightId(Long passengerId, Long flightId);

    List<PassengerCheckIn> findByFlightId(Long flightId);

    boolean existsByPassengerIdAndFlightId(Long passengerId, Long flightId);
}
