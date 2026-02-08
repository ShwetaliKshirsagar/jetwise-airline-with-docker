package com.jetwise_airline.payment_service.controller;

import com.jetwise_airline.payment_service.dto.PaymentRequestDTO;
import com.jetwise_airline.payment_service.service.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentServiceImpl paymentService;
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequestDTO request) {
        String status = paymentService.processPayment(request);
        return ResponseEntity.ok("BOOKING."+status);
    }
}
