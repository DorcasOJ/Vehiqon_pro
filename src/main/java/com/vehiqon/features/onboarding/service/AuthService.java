package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.request.*;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.security.model.CustomerUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.UUID;

public interface AuthService {
    UserResponse register(UserDto.CreateUserRequest request, HttpServletRequest httpServletRequest);
    LoginResponse login(AuthDto.LoginRequest request, HttpServletRequest httpServletRequest);

    LoginResponse refresh(AuthDto.RefreshTokenRequest request, HttpServletRequest httpRequest);

    String logout(AuthDto.LogoutRequest request, HttpServletRequest httpServletRequest);
    String logoutAll(HttpServletRequest httpServletRequest);

    String verifyEmail(String token, HttpServletRequest httpServletRequest);

    String resendVerificationEmail( AuthDto.ResendVerificationRequest request, HttpServletRequest httpServletRequest);

    CustomerUserDetails getAuthenticatedUser();

    String changePassword(AuthDto.ChangePasswordRequest request, HttpServletRequest httpServletRequest);
    String forgotPassword(AuthDto.ForgotPasswordRequest request, HttpServletRequest httpServletRequest );
    String resetPassword(AuthDto.ResetPasswordRequest request, HttpServletRequest httpServletRequest);
}
