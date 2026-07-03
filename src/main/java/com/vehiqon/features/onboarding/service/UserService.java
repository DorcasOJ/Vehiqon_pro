package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.request.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;


public interface UserService {
    UserResponse createUser (CreateUserRequest userRequest);
    void validateUserEmail(UserEntity user );
}
