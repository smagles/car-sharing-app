package com.mate.carsharing.repository;

import com.mate.carsharing.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
