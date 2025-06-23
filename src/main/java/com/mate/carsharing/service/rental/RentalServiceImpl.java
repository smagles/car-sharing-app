package com.mate.carsharing.service.rental;

import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.dto.rental.RentalCreatedEvent;
import com.mate.carsharing.dto.rental.RentalDetailedResponseDto;
import com.mate.carsharing.dto.rental.RentalDto;
import com.mate.carsharing.exception.custom.RentalAlreadyReturnedException;
import com.mate.carsharing.mapper.RentalMapper;
import com.mate.carsharing.model.Car;
import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import com.mate.carsharing.repository.RentalRepository;
import com.mate.carsharing.service.car.CarService;
import com.mate.carsharing.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final CarService carService;
    private final UserService userService;
    private final RentalCreatedEventListener eventListener;

    @Override
    @Transactional
    public RentalDto createRental(RentalCreateRequestDto requestDto, User user) {
        Car car = carService.reserveCar(requestDto.getCarId());
        Rental newRental = rentalMapper.toEntity(requestDto, car, user);
        newRental = rentalRepository.save(newRental);

        eventListener.handleRentalCreated(new RentalCreatedEvent(newRental, car, user));
        return rentalMapper.toDto(newRental);
    }

    @Override
    public RentalDetailedResponseDto getRentalById(Long rentalId) {
        return rentalMapper.toDetailedDto(findRentalById(rentalId));
    }

    @Override
    public RentalDetailedResponseDto getRentalById(Long rentalId, User user) {
        Rental rental = findRentalByIdAndUser(rentalId, user.getId());
        return rentalMapper.toDetailedDto(rental);
    }

    @Override
    public Page<RentalDto> getRentalsByUser(User user, boolean isActive, Pageable pageable) {
        return rentalRepository.findByUserAndIsActive(user, isActive, pageable)
                .map(rentalMapper::toDto);
    }

    @Override
    public Page<RentalDto> getRentalsByUserId(Long userId, boolean isActive, Pageable pageable) {
        User user = userService.findUserById(userId);
        return rentalRepository.findByUserAndIsActive(user, isActive, pageable)
                .map(rentalMapper::toDto);
    }

    @Override
    public Page<RentalDto> getAllRentals(Pageable pageable) {
        return rentalRepository.findAll(pageable)
                .map(rentalMapper::toDto);
    }

    @Override
    @Transactional
    public RentalDto returnRental(Long rentalId, User user) {
        Rental rental = findRentalByIdAndUser(rentalId, user.getId());
        validateRentalIsActive(rental);

        rental.setIsActive(false);
        rental.setActualReturnDate(LocalDate.now());
        carService.returnCar(rental.getCar().getId());

        Rental saved = rentalRepository.save(rental);
        return rentalMapper.toDto(saved);
    }

    private Rental findRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Rental not found with id: " + rentalId));
    }

    private Rental findRentalByIdAndUser(Long rentalId, Long userId) {
        return rentalRepository.findByIdAndUserId(rentalId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Rental not found with id: " + rentalId
                                + " and user id: " + userId
                ));
    }

    private void validateRentalIsActive(Rental rental) {
        if (!rental.getIsActive()) {
            throw new RentalAlreadyReturnedException(rental.getId());
        }
    }

}
