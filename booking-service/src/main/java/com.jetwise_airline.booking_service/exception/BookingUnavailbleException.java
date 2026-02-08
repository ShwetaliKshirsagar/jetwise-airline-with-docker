package com.jetwise_airline.booking_service.exception;

import java.util.Set;

public class BookingUnavailbleException extends RuntimeException {
    private Set<String> missingIds;
    public BookingUnavailbleException(String message){
        super(message);
    }
    public BookingUnavailbleException(Set<String> missingId){
        super("Invalid booking ids :"+ missingId);
        this.missingIds=missingId;
    }
}
