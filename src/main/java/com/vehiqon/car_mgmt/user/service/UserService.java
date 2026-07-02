package com.vehiqon.car_mgmt.user.service;
import com.vehiqon.car_mgmt.auth.dto.CreateUserRequest;
import com.vehiqon.car_mgmt.user.dto.response.UserResponse;
import com.vehiqon.car_mgmt.common.dto.response.ApiResponse;


public interface UserService {
    ApiResponse<UserResponse> createUser (CreateUserRequest userRequest);
}
