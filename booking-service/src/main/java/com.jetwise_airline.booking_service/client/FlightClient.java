package com.jetwise_airline.booking_service.client;

import com.jetwise_airline.booking_service.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-service", url = "http://localhost:8082/api/flights")
public interface FlightClient {
    @GetMapping("/getFlight/{flightId}")
    FlightDTO getFlightById(@PathVariable Long flightId);
}
