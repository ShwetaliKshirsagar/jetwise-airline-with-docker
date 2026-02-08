package com.jetwise_airline.flight_service.repository;

import com.jetwise_airline.flight_service.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
    Optional<List<FlightEntity>> findBySourceAndDestination(String source, String destination);
    Optional<FlightEntity> findByFlightNumber(String flightNumber);
}
