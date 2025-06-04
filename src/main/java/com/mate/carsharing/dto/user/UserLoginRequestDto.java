package com.mate.carsharing.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
        @NotBlank @Email String email,
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        @NotBlank String password) {
}
