package com.vehiqon.features.onboarding.dto.response;

import com.vehiqon.security.authorization.enums.RoleEnum;

import java.util.Set;
import java.util.UUID;

public record UserResponse (
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    Set<RoleEnum> roles,
    String status,
    String gender
){}

