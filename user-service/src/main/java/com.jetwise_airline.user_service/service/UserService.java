package com.jetwise_airline.user_service.service;

import com.jetwise_airline.user_service.dto.LoginUser;
import com.jetwise_airline.user_service.dto.RegisterUser;
import com.jetwise_airline.user_service.dto.UserResponse;
import com.jetwise_airline.user_service.exceptions.InvalidCredentialsException;
import com.jetwise_airline.user_service.exceptions.UserAlreadyExistsException;

public interface UserService {
    UserResponse register(RegisterUser registerUser) throws UserAlreadyExistsException;
    String login(LoginUser loginUser) throws InvalidCredentialsException;

}
