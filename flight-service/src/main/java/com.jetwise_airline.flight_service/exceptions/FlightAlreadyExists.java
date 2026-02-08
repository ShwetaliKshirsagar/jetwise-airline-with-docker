package com.jetwise_airline.flight_service.exceptions;

public class FlightAlreadyExists extends RuntimeException {
    public FlightAlreadyExists (String message){
        super(message);
    }
}
