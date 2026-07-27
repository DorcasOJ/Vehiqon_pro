package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.request.*;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Registration & Authentication")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ApiResponseMapper apiResponseMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> register(@Valid @RequestBody UserDto.CreateUserRequest request,
                                                                      HttpServletRequest httpServletRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                apiResponseMapper.toResponse(authService.register(request, httpServletRequest))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody AuthDto.LoginRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.login(request, httpRequest)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody AuthDto.RefreshTokenRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.refresh(request, httpRequest)));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(apiResponseMapper.toResponse( authService.verifyEmail(token, httpServletRequest)));

    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<ApiResponse<String>> resendVerificationEmail(
            @Valid @RequestBody AuthDto.ResendVerificationRequest request,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.resendVerificationEmail(request, httpServletRequest)));

    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody AuthDto.ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.changePassword(request, httpServletRequest)));

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody AuthDto.ForgotPasswordRequest request,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.forgotPassword(request, httpServletRequest)));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody AuthDto.ResetPasswordRequest request,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.resetPassword(request, httpServletRequest)));
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody AuthDto.LogoutRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse( authService.logout(request, httpServletRequest))
        );
    }

    @PostMapping("/logout/all")
    public ResponseEntity<ApiResponse<String>> logoutAll(HttpServletRequest httpServletRequest) {

        return ResponseEntity.ok(
                apiResponseMapper.toResponse(authService.logoutAll(httpServletRequest))
        );
    }
}

