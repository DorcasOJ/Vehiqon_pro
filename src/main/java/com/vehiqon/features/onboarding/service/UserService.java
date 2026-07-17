package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.request.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.request.UpdateUserRequest;
import com.vehiqon.features.onboarding.dto.request.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;


public interface UserService {
    UserResponse createUser (UserDto.CreateUserRequest userRequest);
    void validateUserEmail(UserEntity user );
    UserResponse updateProfile(UserDto.UpdateUserRequest request);
    UserResponse getProfile();
    void updateRoles(UUID userId, UserDto.UpdateRolesRequest request);
    void syncRoles(UUID userId, UserDto.SyncRolesRequest request);
    void unlockUser(UUID userId, HttpServletRequest request);
}
