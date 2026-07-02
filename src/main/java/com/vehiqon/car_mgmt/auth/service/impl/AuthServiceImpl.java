package com.vehiqon.car_mgmt.auth.service.impl;

import com.vehiqon.car_mgmt.auth.dto.CreateUserRequest;
import com.vehiqon.car_mgmt.auth.dto.LoginResponse;
import com.vehiqon.car_mgmt.auth.dto.request.LoginRequest;
import com.vehiqon.car_mgmt.auth.service.AuthService;
import com.vehiqon.car_mgmt.common.dto.response.ApiResponse;
import com.vehiqon.car_mgmt.user.dto.response.UserResponse;

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
