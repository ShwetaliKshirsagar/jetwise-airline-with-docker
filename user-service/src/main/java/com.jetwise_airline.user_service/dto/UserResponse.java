package com.jetwise_airline.user_service.dto;

import com.jetwise_airline.user_service.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String userName;
    private Role role;
}
