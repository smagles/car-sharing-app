package com.mate.carsharing.service.car;

import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import com.mate.carsharing.mapper.CarMapper;
import com.mate.carsharing.model.Car;
import com.mate.carsharing.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    private Car createTestCar() {
        return Car.builder()
                .id(1L)
                .brand("Toyota")
                .model("Corolla")
                .carType(Car.CarType.SEDAN)
                .dailyFee(BigDecimal.valueOf(200))
                .inventory(5)
                .build();
    }

    private CarDto createTestCarDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .carType(car.getCarType())
                .dailyFee(car.getDailyFee())
                .inventory(car.getInventory())
                .build();
    }

    @Test
    @DisplayName("Verify createCar() method works correctly")
    void createCar_ValidRequest_ReturnsCarDto() {
        // Given
        CarCreateRequestDto requestDto = new CarCreateRequestDto(
                "Toyota", "Corolla",
               "SEDAN", 5,  BigDecimal.valueOf(200));
        Car car = createTestCar();
        CarDto carDto = createTestCarDto(car);

        when(carMapper.toModel(requestDto)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(carDto);

        // When
        CarDto result = carService.createCar(requestDto);

        // Then
        assertThat(result).isEqualTo(carDto);
        verify(carMapper).toModel(requestDto);
        verify(carRepository).save(car);
        verify(carMapper).toDto(car);
        verifyNoMoreInteractions(carMapper, carRepository);
    }

    @Test
    @DisplayName("Verify getCarById() returns valid car")
    void getCarById_ValidId_ReturnsCarDto() {
        // Given
        Car car = createTestCar();
        CarDto carDto = createTestCarDto(car);
        Long carId = car.getId();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(carDto);

        // When
        CarDto result = carService.getCarById(carId);

        // Then
        assertThat(result).isEqualTo(carDto);
        verify(carRepository).findById(carId);
        verify(carMapper).toDto(car);
        verifyNoMoreInteractions(carRepository, carMapper);
    }

    @Test
    @DisplayName("Verify getCarById() throws exception when car not found")
    void getCarById_NonExistingId_ThrowsException() {
        // Given
        Long carId = 99L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // When / Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.getCarById(carId));

        assertThat(exception.getMessage()).isEqualTo("Car not found with id: " + carId);
        verify(carRepository).findById(carId);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("Verify getAllCars() returns list of car DTOs")
    void getAllCars_ValidPageable_ReturnsAllCarsDto() {
        // Given
        Car car = createTestCar();
        CarDto carDto = createTestCarDto(car);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Car> carPage = new PageImpl<>(List.of(car));

        when(carRepository.findAll(pageable)).thenReturn(carPage);
        when(carMapper.toDto(car)).thenReturn(carDto);

        // When
        Page<CarDto> result = carService.getAllCars(pageable);

        // Then
        assertThat(result.getContent().get(0)).isEqualTo(carDto);
        verify(carRepository, times(1)).findAll(pageable);
        verify(carMapper, times(1)).toDto(car);
        verifyNoMoreInteractions(carRepository, carMapper);
    }

    @Test
    @DisplayName("Verify updateCar() works with valid data")
    void updateCar_ValidRequest_ReturnsUpdatedDto() {
        // Given
        Long carId = 1L;
        Car existingCar = createTestCar();
        CarUpdateRequestDto requestDto = new CarUpdateRequestDto(
                "Tesla", "Model 3", "SEDAN", 5,
                BigDecimal.valueOf(500));

        Car updatedCar = Car.builder()
                .id(carId)
                .brand(requestDto.brand())
                .model(requestDto.model())
                .carType(Car.CarType.valueOf(requestDto.carType()))
                .dailyFee(requestDto.dailyFee())
                .inventory(requestDto.inventory())
                .build();

        CarDto updatedDto = createTestCarDto(updatedCar);

        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carMapper.toModel(existingCar, requestDto)).thenReturn(updatedCar);
        when(carRepository.save(updatedCar)).thenReturn(updatedCar);
        when(carMapper.toDto(updatedCar)).thenReturn(updatedDto);

        // When
        CarDto result = carService.updateCar(carId, requestDto);

        // Then
        assertThat(result).isEqualTo(updatedDto);
        verify(carRepository).findById(carId);
        verify(carMapper).toModel(existingCar, requestDto);
        verify(carRepository).save(updatedCar);
        verify(carMapper).toDto(updatedCar);
    }

    @Test
    @DisplayName("Verify updateCar() throws exception for non-existent car")
    void updateCar_NonExistingId_ThrowsException() {
        // Given
        Long carId = 42L;
        CarUpdateRequestDto requestDto = new CarUpdateRequestDto(
                "Ford", "Focus", "SEDAN",
                3, BigDecimal.valueOf(180));
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> carService.updateCar(carId, requestDto));
        verify(carRepository).findById(carId);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("Verify deleteCar() deletes car by id")
    void deleteCar_ExistingId_DeletesSuccessfully() {
        // Given
        Long carId = 1L;
        Car car = createTestCar();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // When
        carService.deleteCar(carId);

        // Then
        verify(carRepository).findById(carId);
        verify(carRepository).deleteById(carId);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("Verify deleteCar() throws exception if car not found")
    void deleteCar_NonExistingId_ThrowsException() {
        // Given
        Long carId = 99L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> carService.deleteCar(carId));
        verify(carRepository).findById(carId);
        verifyNoMoreInteractions(carRepository);
    }
}
