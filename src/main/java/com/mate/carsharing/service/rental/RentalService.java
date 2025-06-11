package com.mate.carsharing.service.rental;

import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.dto.rental.RentalDetailedResponseDto;
import com.mate.carsharing.dto.rental.RentalDto;
import com.mate.carsharing.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalDto createRental(RentalCreateRequestDto requestDto, User user);

    RentalDetailedResponseDto getRentalById(Long rentalId);

    RentalDetailedResponseDto getRentalById(Long rentalId, User user);

    Page<RentalDto> getRentalsByUser(Pageable pageable, User user);

}
