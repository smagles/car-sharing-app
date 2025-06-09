package com.mate.carsharing.dto.car;

import com.mate.carsharing.model.Car;
import com.mate.carsharing.validation.EnumValidator;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CarCreateRequestDto(
        @NotBlank(message = "Model cannot be blank")
        @Size(max = 255, message = "Model must be less than 255 characters")
        String model,

        @NotBlank(message = "Brand cannot be blank")
        @Size(max = 255, message = "Brand must be less than 255 characters")
        String brand,

        @NotBlank(message = "Car type is required")
        @EnumValidator(enumClass = Car.CarType.class,
                message = "Invalid car type. Must be one of: SEDAN, SUV, HATCHBACK, UNIVERSAL")
        String carType,

        @NotNull(message = "Inventory is required")
        @Positive(message = "Inventory cannot be negative")
        Integer inventory,

        @NotNull(message = "Daily fee is required")
        @DecimalMin(value = "0.01", message = "Daily fee must be at least 0.01")
        @Digits(integer = 10, fraction = 2, message = "Invalid daily fee format")
        BigDecimal dailyFee
){}
