package com.mate.carsharing.controller;

import com.mate.carsharing.dto.user.RoleUpdateRequestDto;
import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserUpdateRequestDto;
import com.mate.carsharing.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Updates the role of a user identified by their ID.
     *
     * Only users with the MANAGER role can access this endpoint.
     *
     * @param id the ID of the user whose role is to be updated
     * @param request the role update request containing the new role
     * @return the updated user information
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/role")
    public UserDto updateUserRole(
            @PathVariable Long id,
            @RequestBody @Valid RoleUpdateRequestDto request) {
        return userService.updateUserRole(id, request.role());
    }

    /**
     * Retrieves information about the currently authenticated user.
     *
     * @param authentication the authentication context containing the current user's details
     * @return the current user's information as a UserDto
     */
    @GetMapping("/me")
    public UserDto getCurrentUserInfo(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserInfo(email);
    }

    /**
     * Updates the information of the currently authenticated user.
     *
     * @param request the user information update request
     * @return the updated user data
     */
    @PutMapping("/me")
    public UserDto updateCurrentUserInfo(
            Authentication authentication,
            @RequestBody @Valid UserUpdateRequestDto request) {
        String email = authentication.getName();
        return userService.updateUserInfo(email, request);
    }
}
