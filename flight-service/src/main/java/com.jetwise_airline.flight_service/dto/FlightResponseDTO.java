package com.jetwise_airline.flight_service.dto;

import com.jetwise_airline.flight_service.entity.FlightEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class FlightResponseDTO {
    private Long id;
    private String flightNumber;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int capacity;
    private double price;

}
