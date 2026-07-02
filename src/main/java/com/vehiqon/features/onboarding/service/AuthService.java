package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.LoginResponse;
import com.vehiqon.features.onboarding.dto.request.LoginRequest;
import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;

public interface AuthService {
    ApiResponse<UserResponse> register(CreateUserRequest request);
    ApiResponse<LoginResponse> login(LoginRequest request);
}
