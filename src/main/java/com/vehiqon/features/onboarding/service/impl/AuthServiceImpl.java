package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.features.onboarding.dto.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.LoginResponse;
import com.vehiqon.features.onboarding.dto.request.LoginRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.common.dto.response.ApiResponse;

public class AuthServiceImpl implements AuthService {
    @Override
    public ApiResponse<UserResponse> register(CreateUserRequest request) {
        return null;
    }

    @Override
    public ApiResponse<LoginResponse> register(LoginRequest request) {
        return null;
    }
}
