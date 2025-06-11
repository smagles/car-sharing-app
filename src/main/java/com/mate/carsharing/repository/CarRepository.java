package com.mate.carsharing.repository;

import com.mate.carsharing.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Modifying
    @Query("update Car c set c.inventory = c.inventory - 1 "
            + "where c.id = :id and c.inventory > 0")
    int decrementInventoryIfAvailable(@Param("id") Long id);

}
