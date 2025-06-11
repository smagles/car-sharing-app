package com.mate.carsharing.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.dto.rental.RentalDetailedResponseDto;
import com.mate.carsharing.dto.rental.RentalDto;
import com.mate.carsharing.model.RoleName;
import com.mate.carsharing.model.User;
import com.mate.carsharing.service.rental.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalDto createRental(@RequestBody @Valid RentalCreateRequestDto rentalCreateRequestDto,
                                  @AuthenticationPrincipal User user) {
        return rentalService.createRental(rentalCreateRequestDto, user);
    }

    @GetMapping("/{id}")
    public RentalDetailedResponseDto getRental(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        if (user.getRole() == RoleName.MANAGER) {
            return rentalService.getRentalById(id);
        }

        return rentalService.getRentalById(id, user);
    }

    @GetMapping
    public Page<RentalDto> getRentals(
            @PageableDefault(size = 10, sort = "rentalDate", direction = DESC)
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return rentalService.getRentalsByUser(pageable, user);
    }
}
