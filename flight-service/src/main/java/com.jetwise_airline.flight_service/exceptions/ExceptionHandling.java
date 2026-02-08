package com.jetwise_airline.flight_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(FlightNotFoundException.class)
    private ResponseEntity<Map<String ,Object>> handleFlightNotFoundException(FlightNotFoundException ex){
        HashMap<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("error", ex.getLocalizedMessage());
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(FlightAlreadyExists.class)
    private ResponseEntity<Map<String ,Object>> handleFlightAlreadyExists(FlightAlreadyExists ex){
        HashMap<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("error", ex.getLocalizedMessage());
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralExceptions(Exception ex) {
        HashMap<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("error", ex.getLocalizedMessage());
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);    }


}
