package com.vehiqon.security.authentication.controller;

import com.vehiqon.common.api.dto.response.ApiResponse;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.api.mapper.ApiResponseMapper;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.service.around.AnalyticsAction;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.service.around.AuditAction;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.security.authentication.dto.AuthDto;
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
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> register(
            @Valid @RequestBody UserDto.CreateUserRequest request )
    {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                apiResponseMapper.toResponse(authService.register(request))
        );
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.login(request)));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody AuthDto.RefreshTokenRequest request) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.refresh(request)));
    }


    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(apiResponseMapper.toResponse( authService.verifyEmail(token)));

    }


    @PostMapping("/resend-verification-email")
    public ResponseEntity<ApiResponse<String>> resendVerificationEmail(
            @Valid @RequestBody AuthDto.ResendVerificationRequest request
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.resendVerificationEmail(request)));

    }



    @AnalyticsAction(
            value = EventType.PASSWORD_CHANGED,
            entityIdSource = EntityIdSource.CURRENT_USER
    )
    @AuditAction(
            value = AuditActionType.USER_PASSWORD_CHANGED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.CURRENT_USER
    )
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody AuthDto.ChangePasswordRequest request) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.changePassword(request)));

    }


    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody AuthDto.ForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.forgotPassword(request)));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody AuthDto.ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse(authService.resetPassword(request)));
    }


    @AuditAction(
            value = AuditActionType.USER_LOGGED_OUT,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.CURRENT_USER
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody AuthDto.LogoutRequest request) {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse( authService.logout(request))
        );
    }


    @AuditAction(
            value = AuditActionType.USER_LOGGED_OUT_ALL,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.CURRENT_USER
    )
    @PostMapping("/logout/all")
    public ResponseEntity<ApiResponse<String>> logoutAll(
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");

        return ResponseEntity.ok(
                apiResponseMapper.toResponse(authService.logoutAll(authHeader))
        );
    }
}

