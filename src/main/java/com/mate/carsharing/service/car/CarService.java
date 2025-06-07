package com.mate.carsharing.service.car;

import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import java.util.List;

public interface CarService {
    CarDto createCar(CarCreateRequestDto requestDto);

    CarDto getCarById(Long id);

    List<CarDto> getAllCars();

    CarDto updateCar(Long id, CarUpdateRequestDto requestDto);

    void deleteCar(Long id);
}
