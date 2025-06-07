package com.mate.carsharing.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
        @NotBlank(message = "Firstname is required") String firstName,
        @Size(min = 1, max = 50, message = "Lastname must be between 1 and 50 characters")
        @NotBlank(message = "Lastname is required") String lastName,
        @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
        @Email @NotBlank String email
) {
}
