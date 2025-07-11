package com.mate.carsharing.service.user;

import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserRegistrationRequestDto;
import com.mate.carsharing.dto.user.UserUpdateRequestDto;
import com.mate.carsharing.model.User;

public interface UserService {
    UserDto register(UserRegistrationRequestDto userDto);

    UserDto getUserInfo(String email);

    UserDto updateUserInfo(String email, UserUpdateRequestDto request);

    UserDto updateUserRole(Long userId, String newRole);

    User findUserById(Long id);
}
