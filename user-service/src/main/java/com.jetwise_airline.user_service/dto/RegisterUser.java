package com.jetwise_airline.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterUser {
    @NotNull(message = "Please enter username.")
    @Email(message = "Please enter email id.")
    @NotBlank(message = "Please enter username.")
    private String userName;

    @NotNull(message = "Please enter password.")
    @NotBlank(message = "Please enter password.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long, contain one uppercase, one lowercase, one digit, and one special character" )
    private String password;

}
