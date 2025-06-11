package com.mate.carsharing.service.car;

import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import com.mate.carsharing.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarDto createCar(CarCreateRequestDto requestDto);

    CarDto getCarById(Long id);

    Page<CarDto> getAllCars(Pageable pageable);

    CarDto updateCar(Long id, CarUpdateRequestDto requestDto);

    void deleteCar(Long id);

    Car reserveCar(Long carId);
}
