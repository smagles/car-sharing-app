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

    /**
     * Registers a new user with the provided registration details.
     *
     * Validates that the email is not already registered, encodes the password, saves the new user, and returns the created user as a DTO.
     *
     * @param userRegistrationRequestDto the registration details for the new user
     * @return the registered user's data transfer object
     * @throws RegistrationException if a user with the given email already exists
     */
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

    /**
     * Retrieves user information by email and returns it as a data transfer object.
     *
     * @param email the email address of the user to retrieve
     * @return the user's information as a UserDto
     * @throws EntityNotFoundException if no user with the given email exists
     */
    @Override
    public UserDto getUserInfo(String email) {
        User user = findUserByEmail(email);
        return userMapper.toDto(user);
    }

    /**
     * Updates the information of a user identified by email with the provided update request data.
     *
     * @param email the email address of the user to update
     * @param request the user update request containing new user information
     * @return the updated user as a data transfer object
     */
    @Override
    @Transactional
    public UserDto updateUserInfo(String email, UserUpdateRequestDto request) {
        User user = findUserByEmail(email);
        user = userMapper.toModel(user, request);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Updates the role of a user identified by user ID.
     *
     * @param userId the ID of the user whose role is to be updated
     * @param newRole the new role name as a string
     * @return the updated user as a data transfer object
     *
     * @throws EntityNotFoundException if no user with the given ID exists
     * @throws IllegalArgumentException if the provided role name is invalid
     */
    @Override
    @Transactional
    public UserDto updateUserRole(Long userId, String newRole) {
        User user = findUserById(userId);
        user.setRole(RoleName.valueOf(newRole));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Validates that the provided email is not already registered.
     *
     * @param userRegistrationRequestDto the registration request containing the email to check
     * @throws RegistrationException if a user with the given email already exists
     */
    private void validateRegistration(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.email()).isPresent()) {
            throw new RegistrationException("User with email "
                    + userRegistrationRequestDto.email()
                    + " already exists");
        }
    }

    /****
     * Retrieves a user by email or throws an exception if not found.
     *
     * @param email the email address of the user to retrieve
     * @return the User entity corresponding to the given email
     * @throws EntityNotFoundException if no user with the specified email exists
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        "User with email " + email + " not found")
        );
    }

    /****
     * Retrieves a user by their unique ID.
     *
     * @param id the unique identifier of the user
     * @return the User entity corresponding to the given ID
     * @throws EntityNotFoundException if no user with the specified ID exists
     */
    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "User with id " + id + " not found")
        );
    }

}
