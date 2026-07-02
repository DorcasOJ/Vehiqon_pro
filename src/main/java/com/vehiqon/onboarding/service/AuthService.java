package com.vehiqon.onboarding.service;

import com.vehiqon.onboarding.dto.CreateUserRequest;
import com.vehiqon.onboarding.dto.LoginResponse;
import com.vehiqon.onboarding.dto.request.LoginRequest;
import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.onboarding.dto.response.UserResponse;

public interface AuthService {
    ApiResponse<UserResponse> register(CreateUserRequest request);
    ApiResponse<LoginResponse> register(LoginRequest request);
}
