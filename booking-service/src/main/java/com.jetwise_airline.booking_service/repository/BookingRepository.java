package com.jetwise_airline.booking_service.repository;

import com.jetwise_airline.booking_service.entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, String>{
    @Modifying
    @Query(value = "UPDATE Booking b set b.status= 'CANCEL' where b.booking_id IN :booking" , nativeQuery = true)
    @Transactional
    void updateAllStatusCancel(@Param("booking") List<String> bookings);
}
