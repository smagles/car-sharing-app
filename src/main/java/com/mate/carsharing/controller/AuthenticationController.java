package com.mate.carsharing.controller;

import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserLoginRequestDto;
import com.mate.carsharing.dto.user.UserLoginResponseDto;
import com.mate.carsharing.dto.user.UserRegistrationRequestDto;
import com.mate.carsharing.security.AuthenticationService;
import com.mate.carsharing.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param request the user's login credentials
     * @return a response containing authentication details
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Registers a new user with the provided registration details.
     *
     * @param userDto the registration information for the new user
     * @return the registered user's data
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody UserRegistrationRequestDto userDto) {
        return userService.register(userDto);
    }
}
