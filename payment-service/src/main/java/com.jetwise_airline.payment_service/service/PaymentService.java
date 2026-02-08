package com.jetwise_airline.payment_service.service;

import com.jetwise_airline.payment_service.dto.PaymentRequestDTO;

public interface PaymentService {
     String processPayment(PaymentRequestDTO paymentRequestDTO);
     void cancelBookingsPaymentFailure();
}
