package com.mate.carsharing.mapper;

import com.mate.carsharing.config.MapperConfig;
import com.mate.carsharing.dto.payment.PaymentDto;
import com.mate.carsharing.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(target = "rentalId", source = "rental.id")
    PaymentDto toDto(Payment payment);
}
