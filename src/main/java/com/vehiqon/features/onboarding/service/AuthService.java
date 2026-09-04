package com.vehiqon.features.onboarding.service;

import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.security.authentication.dto.AuthDto;
import com.vehiqon.security.authentication.model.CustomerUserDetails;

public interface AuthService {
    UserDto.UserResponse register(UserDto.CreateUserRequest request);
    LoginResponse login(AuthDto.LoginRequest request);

    LoginResponse refresh(AuthDto.RefreshTokenRequest request);

    String logout(AuthDto.LogoutRequest request);
    String logoutAll(String bearerToken);

    String verifyEmail(String token);

    String resendVerificationEmail( AuthDto.ResendVerificationRequest request );

    CustomerUserDetails getAuthenticatedUser();
    boolean isAdmin();
    String changePassword(AuthDto.ChangePasswordRequest request);
    String forgotPassword(AuthDto.ForgotPasswordRequest request );
    String resetPassword(AuthDto.ResetPasswordRequest request);
}
