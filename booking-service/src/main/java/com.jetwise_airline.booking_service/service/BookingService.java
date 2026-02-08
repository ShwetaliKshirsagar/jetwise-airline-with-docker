package com.jetwise_airline.booking_service.service;

import com.itextpdf.text.DocumentException;
import com.jetwise_airline.booking_service.dto.BookingPaymentResponse;
import com.jetwise_airline.booking_service.dto.BookingRequest;
import com.jetwise_airline.booking_service.exception.BookingUnavailbleException;
import com.jetwise_airline.booking_service.exception.SeatsUnvailableException;

import java.io.FileNotFoundException;
import java.util.List;

public interface BookingService {
    void createBooking(BookingRequest bookingRequest) throws SeatsUnvailableException;

    BookingPaymentResponse getBookingDetails(String bookingId) throws BookingUnavailbleException;

    void updateBookingStatus(String bookingId, String status);

    void generateTicket(String bookingId) throws BookingUnavailbleException, FileNotFoundException, DocumentException;

    void updateBookingStatusCancel(List<String> bookings);
}
