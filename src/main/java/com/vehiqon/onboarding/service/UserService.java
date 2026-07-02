package com.vehiqon.onboarding.service;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.onboarding.dto.CreateUserRequest;
import com.vehiqon.onboarding.dto.response.UserResponse;


public interface UserService {
    ApiResponse<UserResponse> createUser (CreateUserRequest userRequest);
}
