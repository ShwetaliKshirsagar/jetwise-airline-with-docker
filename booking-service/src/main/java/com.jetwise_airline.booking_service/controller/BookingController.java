package com.jetwise_airline.booking_service.controller;

import com.itextpdf.text.DocumentException;
import com.jetwise_airline.booking_service.dto.BookingPaymentResponse;
import com.jetwise_airline.booking_service.dto.BookingRequest;
import com.jetwise_airline.booking_service.service.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingServiceImpl bookingService;
    @PostMapping("create")
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.createBooking(bookingRequest);
        return new ResponseEntity<>("Please proceed for payment to confirm booking", HttpStatus.CREATED);
    }
    @GetMapping("fetch/{bookingid}")
    public ResponseEntity<BookingPaymentResponse> getBookingDetails(@PathVariable ("bookingid") String bookingid){
        BookingPaymentResponse bookingDetails = bookingService.getBookingDetails(bookingid);
        return new ResponseEntity<>(bookingDetails,HttpStatus.OK);
    }
    @PutMapping("update-status/{bookingid}/{status}")
    public ResponseEntity<String> updateBookingStatus(@PathVariable ("bookingid") String bookingid, @PathVariable ("status") String status){
        bookingService.updateBookingStatus(bookingid,status);
        return new ResponseEntity<>("UPDATED.BOOKING.STATUS",HttpStatus.OK);
    }
    @PostMapping("/generateTicket/{bookingId}")
    public ResponseEntity<Void> generateTicket(@PathVariable String bookingId) throws DocumentException, FileNotFoundException {
        bookingService.generateTicket(bookingId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/cancel")
    public ResponseEntity<String> updateBookingStatusCancel(@RequestBody List<String> bookings){
        bookingService.updateBookingStatusCancel(bookings);
        return new ResponseEntity<>("UPDATED.BOOKING.STATUS.CANCEL",HttpStatus.OK);
    }
}
