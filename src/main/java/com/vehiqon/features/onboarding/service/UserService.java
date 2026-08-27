package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface UserService {
    UserDto.UserResponse createUser (UserDto.CreateUserRequest userRequest);
    void validateUserEmail(UserEntity user );
    UserDto.UserResponse updateProfile(UserDto.UpdateUserRequest request, HttpServletRequest httpServletRequest);
    UserDto.UserResponse getProfile();
    void updateRoles(UUID userId, UserDto.UpdateRolesRequest request,  HttpServletRequest httpServletRequest);
    void syncRoles(UUID userId, UserDto.SyncRolesRequest request,  HttpServletRequest httpServletRequest);
    void unlockUser(UUID userId, HttpServletRequest request);
    UserDto.UserResponse getUser(UUID userId);
    void deleteUser(UUID userId);
    void restoreUser(UUID userId);
    void deleteMultipleUser(List<UUID> userId);
    void restoreMultipleUser(List<UUID> userIds);
    Page<UserDto.UserResponse> getAllUser(Pageable pageable);
    Page<UserDto.UserResponse> searchUser(String query, Pageable pageable);
}
