package com.vehiqon.car_mgmt.auth.service;

import com.vehiqon.car_mgmt.auth.dto.CreateUserRequest;
import com.vehiqon.car_mgmt.auth.dto.LoginResponse;
import com.vehiqon.car_mgmt.auth.dto.request.LoginRequest;
import com.vehiqon.car_mgmt.common.dto.response.ApiResponse;
import com.vehiqon.car_mgmt.user.dto.response.UserResponse;

public interface AuthService {
    ApiResponse<UserResponse> register(CreateUserRequest request);
    ApiResponse<LoginResponse> register(LoginRequest request);
}
