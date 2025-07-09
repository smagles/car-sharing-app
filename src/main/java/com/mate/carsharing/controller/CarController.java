package com.mate.carsharing.controller;

import com.mate.carsharing.controller.docs.CarControllerDocs;
import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import com.mate.carsharing.service.car.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController implements CarControllerDocs {
    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    public CarDto createCar(@Valid @RequestBody CarCreateRequestDto requestDto) {
        return carService.createCar(requestDto);
    }

    @GetMapping
    public Page<CarDto> getAllCars(@PageableDefault(size = 10) Pageable pageable) {
        return carService.getAllCars(pageable);
    }

    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public CarDto updateCar(@PathVariable Long id,
                            @Valid @RequestBody CarUpdateRequestDto requestDto) {
        return carService.updateCar(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
