package com.vehiqon.features.onboarding.service;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.onboarding.dto.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;


public interface UserService {
    UserResponse createUser (CreateUserRequest userRequest);
}
