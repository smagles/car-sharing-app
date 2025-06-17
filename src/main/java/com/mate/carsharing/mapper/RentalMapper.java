package com.mate.carsharing.mapper;

import com.mate.carsharing.config.MapperConfig;
import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.dto.rental.RentalDetailedResponseDto;
import com.mate.carsharing.dto.rental.RentalDto;
import com.mate.carsharing.model.Car;
import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {CarMapper.class, UserMapper.class})
public interface RentalMapper {
    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "isActive", source = "isActive")
    RentalDto toDto(Rental rental);

    @Mapping(target = "id", ignore = true)
    Rental toEntity(RentalCreateRequestDto rentalCreateRequestDto, Car car, User user);

    RentalDetailedResponseDto toDetailedDto(Rental rental);
}
