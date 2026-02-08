package com.jetwise_airline.payment_service.service;

import com.jetwise_airline.payment_service.client.BookingClient;
import com.jetwise_airline.payment_service.dto.PaymentRequestDTO;
import com.jetwise_airline.payment_service.entity.Payment;
import com.jetwise_airline.payment_service.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BookingClient bookingClient;
    @Override
    public String processPayment(PaymentRequestDTO request) {


        boolean success = Math.random() > 0.2; // 80% success rate

        String status = success ? "SUCCESS" : "FAILED";
        Payment payment = PaymentRequestDTO.fromDTOToEntity(request);
        payment.setStatus(status);

        paymentRepository.save(payment);
        String s = bookingClient.updateBookingStatus(request.getBookingId(), status);
        return status;

    }

    @Override
    @Scheduled(fixedDelay = 5000)
    @Async
    public void cancelBookingsPaymentFailure() {
        System.out.println("Calling cancel booking every 5sec using scheduling");
        List<String> bookingIdPaymentFailure = paymentRepository.getBookingIdPaymentFailure();
        bookingClient.updateBookingStatusCancel(bookingIdPaymentFailure);
    }



}
