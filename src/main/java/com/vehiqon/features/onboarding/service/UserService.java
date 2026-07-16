package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.request.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.request.UpdateUserRequest;
import com.vehiqon.features.onboarding.dto.request.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;


public interface UserService {
    UserResponse createUser (UserDto.CreateUserRequest userRequest);
    void validateUserEmail(UserEntity user );
    UserResponse updateProfile(UserDto.UpdateUserRequest request);
    UserResponse getProfile();
}
