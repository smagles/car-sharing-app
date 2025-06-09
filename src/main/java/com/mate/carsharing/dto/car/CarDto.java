package com.mate.carsharing.dto.car;

import com.mate.carsharing.model.Car;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long id;
    private String model;
    private String brand;
    private Car.CarType carType;
    private int inventory;
    private BigDecimal dailyFee;
}
