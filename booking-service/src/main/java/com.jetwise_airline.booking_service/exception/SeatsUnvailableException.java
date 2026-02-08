package com.jetwise_airline.booking_service.exception;

public class SeatsUnvailableException extends RuntimeException {
public SeatsUnvailableException(String message){
    super(message);
}
}
