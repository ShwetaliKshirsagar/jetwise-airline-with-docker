package com.jetwise_airline.payment_service.repository;

import com.jetwise_airline.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {
    @Query(value = "SELECT p.booking_id FROM payment p WHERE STATUS ='FAILED'", nativeQuery = true)
    List<String> getBookingIdPaymentFailure();
}
