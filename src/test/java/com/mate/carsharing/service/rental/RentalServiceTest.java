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
import com.mate.carsharing.service.payment.BorrowingValidationService;
import com.mate.carsharing.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private CarService carService;
    @Mock
    private UserService userService;
    @Mock
    private RentalCreatedEventListener eventListener;
    @Mock
    private BorrowingValidationService borrowingValidationService;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private User user;
    private Car car;
    private Rental rental;
    private RentalCreateRequestDto requestDto;
    private RentalDto rentalDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        car = new Car();
        car.setId(1L);

        rental = new Rental();
        rental.setId(1L);
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(3));
        rental.setIsActive(true);
        rental.setCar(car);
        rental.setUser(user);

        requestDto = new RentalCreateRequestDto();
        requestDto.setCarId(1L);
        requestDto.setReturnDate(LocalDate.now().plusDays(3));

        rentalDto = new RentalDto();
        rentalDto.setId(1L);
    }

    @Test
    @DisplayName("Verify createRental(): should create a rental successfully when all inputs are valid")
    void createRental_shouldCreateRentalSuccessfully() {
        // Given
        when(carService.reserveCar(1L)).thenReturn(car);
        when(rentalMapper.toEntity(requestDto, car, user)).thenReturn(rental);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);
        when(rentalMapper.toDto(rental)).thenReturn(rentalDto);

        // When
        RentalDto result = rentalService.createRental(requestDto, user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(borrowingValidationService).validateNoPendingPayments(1L);
        verify(eventListener).handleRentalCreated(any(RentalCreatedEvent.class));
    }

    @Test
    @DisplayName("Verify returnRental(): should mark rental as returned and set actual return date")
    void returnRental_shouldMarkRentalAsReturned() {
        // Given
        rental.setIsActive(true);
        when(rentalRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);
        when(rentalMapper.toDto(any(Rental.class))).thenReturn(rentalDto);

        // When
        RentalDto result = rentalService.returnRental(1L, user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(rental.getIsActive()).isFalse();
        assertThat(rental.getActualReturnDate()).isEqualTo(LocalDate.now());

        verify(carService).returnCar(car.getId());
    }

    @Test
    @DisplayName("Verify getRentalById(): should return detailed rental information when rental exists")
    void getRentalById_shouldReturnDetailedDto() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        RentalDetailedResponseDto detailedDto = new RentalDetailedResponseDto();
        when(rentalMapper.toDetailedDto(rental)).thenReturn(detailedDto);

        // When
        RentalDetailedResponseDto result = rentalService.getRentalById(1L);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Verify getRentalsByUser(): should return page of active rentals for a user")
    void getRentalsByUser_shouldReturnPageOfDto() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rental> rentalPage = new PageImpl<>(List.of(rental));
        when(rentalRepository.findByUserAndIsActive(user, true, pageable))
                .thenReturn(rentalPage);
        when(rentalMapper.toDto(rental)).thenReturn(rentalDto);

        // When
        Page<RentalDto> result = rentalService.getRentalsByUser(user, true, pageable);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Verify returnRental(): should throw exception when rental is already returned")
    void returnRental_shouldThrowExceptionIfAlreadyReturned() {
        // Given
        rental.setIsActive(false);
        when(rentalRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(rental));

        // Then
        assertThrows(RentalAlreadyReturnedException.class,
                () -> rentalService.returnRental(1L, user));
    }

    @Test
    @DisplayName("Verify getRentalById(): should throw EntityNotFoundException when rental is not found")
    void getRentalById_shouldThrowExceptionIfNotFound() {
        // Given
        when(rentalRepository.findById(999L)).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> rentalService.getRentalById(999L));
    }

    @Test
    @DisplayName("Verify getRentalByIdAndUser(): should throw EntityNotFoundException when rental is not found for user")
    void getRentalByIdAndUser_shouldThrowIfNotFound() {
        // Given
        when(rentalRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> rentalService.getRentalById(1L, user));
    }

    @Test
    @DisplayName("Verify getAllRentals(): should return page of rentals")
    void shouldReturnAllRentals() {
        Pageable pageable = PageRequest.of(0, 5);
        when(rentalRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(rental)));
        when(rentalMapper.toDto(rental)).thenReturn(rentalDto);

        Page<RentalDto> result = rentalService.getAllRentals(pageable);

        assertThat(result).isNotNull();
        verify(rentalRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Verify getRentalsByUserId(): should return rentals by user ID")
    void shouldReturnRentalsByUserId() {
        Pageable pageable = PageRequest.of(0, 5);
        when(userService.findUserById(1L)).thenReturn(user);
        when(rentalRepository.findByUserAndIsActive(user, true, pageable))
                .thenReturn(new PageImpl<>(List.of(rental)));
        when(rentalMapper.toDto(rental)).thenReturn(rentalDto);

        Page<RentalDto> result = rentalService.getRentalsByUserId(1L, true, pageable);

        assertThat(result).isNotNull();
        verify(rentalRepository).findByUserAndIsActive(user, true, pageable);
    }
}
