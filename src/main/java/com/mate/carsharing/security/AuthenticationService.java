package com.mate.carsharing.security;

import com.mate.carsharing.dto.user.UserLoginRequestDto;
import com.mate.carsharing.dto.user.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user with the provided credentials and returns a JWT token upon successful authentication.
     *
     * @param request the user login request containing email and password
     * @return a response containing the generated JWT token
     */
    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
