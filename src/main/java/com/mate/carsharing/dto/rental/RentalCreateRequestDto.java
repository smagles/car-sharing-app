package com.mate.carsharing.dto.rental;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreateRequestDto {
    @NotNull(message = "Rental date is required")
    @FutureOrPresent(message = "Rental date cannot be in the past")
    private LocalDate rentalDate;

    @NotNull(message = "Return date is required")
    @Future(message = "Return date must be in the future")
    private LocalDate returnDate;

    @NotNull(message = "Car ID is required")
    @Positive(message = "Car ID must be a positive number")
    private Long carId;

    @AssertTrue(message = "Return date must be after rental date")
    public boolean isReturnDateAfterRentalDate() {
        return returnDate != null && rentalDate != null && returnDate.isAfter(rentalDate);
    }
}
