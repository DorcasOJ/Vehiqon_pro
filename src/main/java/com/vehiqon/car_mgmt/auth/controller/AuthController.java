package com.vehiqon.car_mgmt.auth.controller;

import com.vehiqon.car_mgmt.auth.dto.CreateUserRequest;
import com.vehiqon.car_mgmt.auth.service.AuthService;
import com.vehiqon.car_mgmt.common.dto.response.ApiResponse;
import com.vehiqon.car_mgmt.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        return
    }
}
