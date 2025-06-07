package com.mate.carsharing.dto.user;

import com.mate.carsharing.model.RoleName;
import com.mate.carsharing.validation.EnumValidator;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateRequestDto(
        @NotNull(message = "Role is required")
        @EnumValidator(enumClass = RoleName.class, message = "Role must be MANAGER or CUSTOMER")
        String role
) {}
