package com.vehiqon.onboarding.service.impl;

import com.vehiqon.onboarding.dto.CreateUserRequest;
import com.vehiqon.onboarding.dto.LoginResponse;
import com.vehiqon.onboarding.dto.request.LoginRequest;
import com.vehiqon.onboarding.dto.response.UserResponse;
import com.vehiqon.onboarding.service.AuthService;
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
