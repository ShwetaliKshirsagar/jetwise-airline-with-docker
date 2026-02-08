package com.jetwise_airline.user_service.controller;

import com.jetwise_airline.user_service.dto.LoginUser;
import com.jetwise_airline.user_service.dto.RegisterUser;
import com.jetwise_airline.user_service.dto.UserResponse;
import com.jetwise_airline.user_service.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUser registerUser){
        UserResponse registeredUser = userService.register(registerUser);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
    @GetMapping("/login")
   public ResponseEntity<String> login(@RequestBody LoginUser loginUser) {
       String generatedToken = userService.login(loginUser);
       return new ResponseEntity<>(generatedToken, HttpStatus.OK);
   }
    @GetMapping("/validateToken")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        Map<String, Object> response = new HashMap<>();

//        try {
//            String username = jwtService.extractUsername(token);
//            boolean isValid = jwtService.isTokenValid(token, username);
//
//            response.put("valid", isValid);
//            response.put("username", username);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            response.put("valid", false);
//            response.put("error", "Invalid or expired token");
            return null;        }

    }
