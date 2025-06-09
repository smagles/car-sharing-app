package com.mate.carsharing.service.car;

import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import com.mate.carsharing.mapper.CarMapper;
import com.mate.carsharing.model.Car;
import com.mate.carsharing.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<CarDto> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(carMapper::toDto)
                .toList();
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
}
