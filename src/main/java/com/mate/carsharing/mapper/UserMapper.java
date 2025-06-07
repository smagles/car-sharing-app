package com.mate.carsharing.mapper;

import com.mate.carsharing.config.MapperConfig;
import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserRegistrationRequestDto;
import com.mate.carsharing.dto.user.UserUpdateRequestDto;
import com.mate.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    User toModel(@MappingTarget User user, UserUpdateRequestDto userUpdateRequestDto);

    UserDto toDto(User user);

}
