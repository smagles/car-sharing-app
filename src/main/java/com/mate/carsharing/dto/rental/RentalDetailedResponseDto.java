package com.mate.carsharing.dto.rental;

import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.user.UserDto;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalDetailedResponseDto {
    private Long id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private CarDto car;
    private UserDto user;
}
