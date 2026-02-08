package com.jetwise_airline.flight_service.controller;

import com.jetwise_airline.flight_service.dto.FlightBookingResponse;
import com.jetwise_airline.flight_service.dto.FlightRequestDTO;
import com.jetwise_airline.flight_service.dto.FlightResponseDTO;
import com.jetwise_airline.flight_service.service.FlightServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    @Autowired
    private FlightServiceImpl flightService;

    //Add Flight
    @PostMapping("add")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addFlight(@RequestBody FlightRequestDTO flightRequest) {
        flightService.addFlight(flightRequest);
        return new ResponseEntity<>("Flight added successfully", HttpStatus.CREATED);
    }

    // Update Flight
    @PutMapping("update")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightResponseDTO> updateFlight(@RequestBody @Valid FlightRequestDTO flightRequest)
            throws Exception {
        FlightResponseDTO updated = flightService.updateFlight(flightRequest);
        return ResponseEntity.ok(updated);
    }

    //  Delete Flight
    @DeleteMapping("{flightNumber}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightNumber) {
        flightService.deleteFlight(flightNumber);
        return ResponseEntity.ok("Flight deleted successfully");
    }

    //Search Flights
    @GetMapping("search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(
            @RequestParam String source,
            @RequestParam String destination) {
        List<FlightResponseDTO> flights = flightService.searchFlights(source, destination);
        return ResponseEntity.ok(flights);
    }
    //Get flight details for booking, generate ticket
    @GetMapping("getFlight/{flightId}")
    public ResponseEntity<FlightResponseDTO> getFlightById(
          @PathVariable long flightId) {
        FlightResponseDTO flightById = flightService.getFlightById(flightId);
        return ResponseEntity.ok(flightById);
    }
}


