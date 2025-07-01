package com.mate.carsharing.dto.payment;

import com.mate.carsharing.model.Payment;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDto(
        @NotNull(message = "Rental ID must not be null")
        Long rentalId,

        @NotNull(message = "Payment type must not be null")
        Payment.PaymentType type

) {
}
