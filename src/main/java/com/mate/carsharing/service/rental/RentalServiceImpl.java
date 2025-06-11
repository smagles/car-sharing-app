package com.mate.carsharing.service.rental;

import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.dto.rental.RentalDetailedResponseDto;
import com.mate.carsharing.dto.rental.RentalDto;
import com.mate.carsharing.mapper.RentalMapper;
import com.mate.carsharing.model.Car;
import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import com.mate.carsharing.repository.RentalRepository;
import com.mate.carsharing.service.car.CarService;
import com.mate.carsharing.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    @Transactional
    public RentalDto createRental(RentalCreateRequestDto requestDto, User user) {
        Car car = carService.findCarById(requestDto.getCarId());
        carService.reserveCar(car);
        Rental newRental = rentalMapper.toEntity(requestDto, car, user);
        newRental = rentalRepository.save(newRental);
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
    public Page<RentalDto> getRentalsByUser(User user, Pageable pageable) {
        return rentalRepository.findByUser(user, pageable)
                .map(rentalMapper::toDto);
    }

    @Override
    public Page<RentalDto> getRentalsByUserId(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        return rentalRepository.findByUser(user, pageable)
                .map(rentalMapper::toDto);
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
}
