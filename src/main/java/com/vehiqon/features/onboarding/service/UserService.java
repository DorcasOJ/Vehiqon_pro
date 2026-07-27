package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;


public interface UserService {
    UserDto.UserResponse createUser (UserDto.CreateUserRequest userRequest);
    void validateUserEmail(UserEntity user );
    UserDto.UserResponse updateProfile(UserDto.UpdateUserRequest request, HttpServletRequest httpServletRequest);
    UserDto.UserResponse getProfile(HttpServletRequest httpServletRequest);
    void updateRoles(UUID userId, UserDto.UpdateRolesRequest request,  HttpServletRequest httpServletRequest);
    void syncRoles(UUID userId, UserDto.SyncRolesRequest request,  HttpServletRequest httpServletRequest);
    void unlockUser(UUID userId, HttpServletRequest request);
    UserDto.UserResponse getUser(UUID userId, HttpServletRequest request);
}
