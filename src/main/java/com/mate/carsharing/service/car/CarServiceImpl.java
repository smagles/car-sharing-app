package com.mate.carsharing.service.car;

import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import com.mate.carsharing.exception.custom.NoAvailableCarException;
import com.mate.carsharing.mapper.CarMapper;
import com.mate.carsharing.model.Car;
import com.mate.carsharing.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto createCar(CarCreateRequestDto requestDto) {
        Car newCar = carMapper.toModel(requestDto);
        newCar = carRepository.save(newCar);
        return carMapper.toDto(newCar);
    }

    @Override
    public CarDto getCarById(Long id) {
        return carMapper.toDto(findCarById(id));
    }

    @Override
    public Page<CarDto> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable).map(carMapper::toDto);
    }

    @Override
    public CarDto updateCar(Long id, CarUpdateRequestDto requestDto) {
        Car car = findCarById(id);
        car = carMapper.toModel(car, requestDto);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    public void deleteCar(Long id) {
        findCarById(id);
        carRepository.deleteById(id);

    }

    private Car findCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Car not found with id: " + id));
    }

    @Transactional
    @Override
    public Car reserveCar(Long carId) {
        Car car = findCarByIdForUpdate(carId);
        if (car.getInventory() <= 0) {
            throw new NoAvailableCarException(
                    "No available cars for car id: " + car.getId());
        }
        car.setInventory(car.getInventory() - 1);
        return carRepository.save(car);
    }

    private Car findCarByIdForUpdate(Long id) {
        return carRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Car not found with id: " + id));
    }
}
