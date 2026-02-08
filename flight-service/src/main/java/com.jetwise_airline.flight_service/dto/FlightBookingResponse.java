package com.jetwise_airline.flight_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightBookingResponse {
    private Long id;
    private String flightNumber;
    private int capacity;
    private double price;
}
