package com.mate.carsharing.mapper;

import com.mate.carsharing.config.MapperConfig;
import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import com.mate.carsharing.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarDto toDto(Car car);

    Car toModel(CarCreateRequestDto carDto);

    @Mapping(target = "id", ignore = true)
    Car toModel(@MappingTarget Car car, CarUpdateRequestDto requestDto);
}
