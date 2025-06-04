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
    /****
 * Converts a UserRegistrationRequestDto to a User domain model instance.
 *
 * @param userRegistrationRequestDto the registration request data
 * @return a new User populated with data from the registration request
 */
User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    /****
 * Updates the given User model instance with data from a UserUpdateRequestDto.
 *
 * @param user the User instance to update
 * @param userUpdateRequestDto the data transfer object containing updated user information
 * @return the updated User instance
 */
User toModel(@MappingTarget User user, UserUpdateRequestDto userUpdateRequestDto);

    /****
 * Converts a User model instance to a UserDto data transfer object.
 *
 * @param user the User model to convert
 * @return the corresponding UserDto representation
 */
UserDto toDto(User user);

}
