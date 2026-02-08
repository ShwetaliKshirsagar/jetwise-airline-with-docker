package com.jetwise_airline.booking_service.dto;

import com.jetwise_airline.booking_service.entity.Booking;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingPaymentResponse {
    private String bookingId;
    private String bookingStatus;
    private int selectedSeats;
    private long flightId;

}
