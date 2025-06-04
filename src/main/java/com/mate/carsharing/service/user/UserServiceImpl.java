package com.mate.carsharing.service.user;

import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserRegistrationRequestDto;
import com.mate.carsharing.dto.user.UserUpdateRequestDto;
import com.mate.carsharing.exception.custom.RegistrationException;
import com.mate.carsharing.mapper.UserMapper;
import com.mate.carsharing.model.RoleName;
import com.mate.carsharing.model.User;
import com.mate.carsharing.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        validateRegistration(userRegistrationRequestDto);

        User newUser = userMapper.toModel(userRegistrationRequestDto);
        newUser.setPassword(passwordEncoder.encode(userRegistrationRequestDto.password()));
        newUser = userRepository.save(newUser);

        log.info("Successfully registered user with Email: {}", newUser.getEmail());
        return userMapper.toDto(newUser);
    }

    @Override
    public UserDto getUserInfo(String email) {
        User user = findUserByEmail(email);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUserInfo(String email, UserUpdateRequestDto request) {
        User user = findUserByEmail(email);
        user = userMapper.toModel(user, request);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUserRole(Long userId, String newRole) {
        User user = findUserById(userId);
        user.setRole(RoleName.valueOf(newRole));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private void validateRegistration(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.email()).isPresent()) {
            throw new RegistrationException("User with email "
                    + userRegistrationRequestDto.email()
                    + " already exists");
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        "User with email " + email + " not found")
        );
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "User with id " + id + " not found")
        );
    }

}
