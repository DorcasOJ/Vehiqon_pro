package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.onboarding.dto.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.LoginResponse;
import com.vehiqon.features.onboarding.dto.request.LoginRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("Login endpoint reached");
        return authService.login(request);
    }
}

