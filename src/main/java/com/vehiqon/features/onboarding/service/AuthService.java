package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.request.*;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface AuthService {
    ApiResponse<UserResponse> register(CreateUserRequest request);
    ApiResponse<LoginResponse> login(LoginRequest request, HttpServletRequest httpServletRequest);

    ApiResponse<LoginResponse> refresh(RefreshTokenRequest request, HttpServletRequest httpRequest);

    ApiResponse<Void> logout(LogoutRequest request);
    ApiResponse<Void> logoutAll();

    ApiResponse<Void> verifyEmail(String token);

    ApiResponse<Void> resendVerificationEmail(@Valid ResendVerificationRequest request);

    UserEntity getAuthenticatedUser();
}
