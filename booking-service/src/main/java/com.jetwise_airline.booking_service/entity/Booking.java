package com.jetwise_airline.booking_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Booking")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="bookingId", length=36)
    private String bookingId;
    @Column(name="status")
    private String bookingStatus;
    @Column(name="bookedseats")
    private int selectedSeats;
    @Column(name="flightid")
    private long flightId;
    @Column(name="emailid")
    private String emailId;

}
