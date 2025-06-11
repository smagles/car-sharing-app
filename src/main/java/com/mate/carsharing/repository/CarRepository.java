package com.mate.carsharing.repository;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

import com.mate.carsharing.model.Car;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Lock(PESSIMISTIC_WRITE)
    Optional<Car> findById(Long id);
}
