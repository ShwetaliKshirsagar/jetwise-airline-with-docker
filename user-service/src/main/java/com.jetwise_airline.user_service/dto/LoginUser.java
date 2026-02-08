package com.jetwise_airline.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {
    @NotNull(message = "Please enter username.")
    @Email(message = "Please enter email id.")
    @NotBlank(message = "Please enter username.")
    private String userName;
    @NotNull(message = "Please enter password.")
    @NotBlank(message = "Please enter password.")
    private String password;

}
