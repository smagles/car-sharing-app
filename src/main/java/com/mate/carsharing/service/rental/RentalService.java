package com.mate.carsharing.service.rental;

import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.dto.rental.RentalDetailedResponseDto;
import com.mate.carsharing.dto.rental.RentalDto;
import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalDto createRental(RentalCreateRequestDto requestDto, User user);

    RentalDetailedResponseDto getRentalById(Long rentalId);

    RentalDetailedResponseDto getRentalById(Long rentalId, User user);

    Page<RentalDto> getRentalsByUser(User user, boolean isActive, Pageable pageable);

    Page<RentalDto> getRentalsByUserId(Long userId, boolean isActive, Pageable pageable);

    Page<RentalDto> getAllRentals(Pageable pageable);

    RentalDto returnRental(Long rentalId, User user);

    Rental findRentalByIdAndUser(Long rentalId, Long userId);
}
