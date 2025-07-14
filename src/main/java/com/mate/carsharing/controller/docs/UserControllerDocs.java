package com.mate.carsharing.controller.docs;

import com.mate.carsharing.dto.user.RoleUpdateRequestDto;
import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Management", description = "Endpoints for managing user information and roles")
public interface UserControllerDocs {

    @Operation(
            summary = "Update user role",
            description = "Change a user's role (requires MANAGER privileges)",
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role updated successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid role specified"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden - insufficient privileges"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    UserDto updateUserRole(
            @Parameter(in = ParameterIn.PATH, description = "User ID") Long id,
            @RequestBody RoleUpdateRequestDto request);

    @Operation(
            summary = "Get current user info",
            description = "Retrieve information about the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information retrieved",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    UserDto getCurrentUserInfo(Authentication authentication);

    @Operation(
            summary = "Update current user info",
            description = "Update the currently authenticated user's information",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    UserDto updateCurrentUserInfo(Authentication authentication,
                                  @RequestBody UserUpdateRequestDto request);
}
