package com.mate.carsharing.controller;

import com.mate.carsharing.controller.docs.UserControllerDocs;
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
public class UserController implements UserControllerDocs {
    private final UserService userService;

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/role")
    public UserDto updateUserRole(
            @PathVariable Long id,
            @RequestBody @Valid RoleUpdateRequestDto request) {
        return userService.updateUserRole(id, request.role());
    }

    @GetMapping("/me")
    public UserDto getCurrentUserInfo(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserInfo(email);
    }

    @PutMapping("/me")
    public UserDto updateCurrentUserInfo(
            Authentication authentication,
            @RequestBody @Valid UserUpdateRequestDto request) {
        String email = authentication.getName();
        return userService.updateUserInfo(email, request);
    }
}
