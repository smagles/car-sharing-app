package com.mate.carsharing.controller.docs;

import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserLoginRequestDto;
import com.mate.carsharing.dto.user.UserLoginResponseDto;
import com.mate.carsharing.dto.user.UserRegistrationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public interface AuthenticationControllerDocs {

    @Operation(
            summary = "Login",
            description = "Authenticate user and return JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                            content = @Content(schema = @Schema(
                                    implementation = UserLoginResponseDto.class))),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized - invalid credentials")
            }
    )
    UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request);

    @Operation(
            summary = "Register",
            description = "Create a new user account",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "Email already exists")
            }
    )
    UserDto register(@Valid @RequestBody UserRegistrationRequestDto userDto);
}
