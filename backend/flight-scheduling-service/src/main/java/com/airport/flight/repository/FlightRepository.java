package com.airport.flight.repository;
package com.airport.flight.repository;

import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("select f from Flight f where (:status is null or f.status = :status) " +
            "and (:from is null or f.scheduledDeparture >= :from) " +
            "and (:to is null or f.scheduledDeparture <= :to) " +
            "order by f.scheduledDeparture asc")
    List<Flight> findByFilters(@Param("status") FlightStatus status,
                               @Param("from") LocalDateTime from,
                               @Param("to") LocalDateTime to);

    @Query("select f from Flight f where " +
            "(:origin is null or upper(f.origin) = upper(:origin)) and " +
            "(:destination is null or upper(f.destination) = upper(:destination)) and " +
            "(:dayStart is null or f.scheduledDeparture between :dayStart and :dayEnd) " +
            "order by f.scheduledDeparture asc")
    List<Flight> search(@Param("origin") String origin,
                        @Param("destination") String destination,
                        @Param("dayStart") LocalDateTime dayStart,
                        @Param("dayEnd") LocalDateTime dayEnd);
}
import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("select f from Flight f where (:status is null or f.status = :status) " +
            "and (:from is null or f.scheduledDeparture >= :from) " +
            "and (:to is null or f.scheduledDeparture <= :to) " +
            "order by f.scheduledDeparture asc")
    List<Flight> findByFilters(FlightStatus status, LocalDateTime from, LocalDateTime to);

    @Query("select f from Flight f where " +
            "(:origin is null or upper(f.origin) = upper(:origin)) and " +
            "(:destination is null or upper(f.destination) = upper(:destination)) and " +
            "(:dayStart is null or f.scheduledDeparture between :dayStart and :dayEnd) " +
            "order by f.scheduledDeparture asc")
    List<Flight> search(String origin, String destination, LocalDateTime dayStart, LocalDateTime dayEnd);
}
