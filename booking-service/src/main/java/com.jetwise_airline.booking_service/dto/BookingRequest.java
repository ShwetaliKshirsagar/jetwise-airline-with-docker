package com.jetwise_airline.booking_service.dto;


import com.jetwise_airline.booking_service.entity.Booking;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    private int selectedSeats;
    private long flightId;
//    private String emailId;


}

