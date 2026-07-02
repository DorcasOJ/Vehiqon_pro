package com.casdore.car_mgmt.user.service;
import com.casdore.car_mgmt.auth.dto.CreateUserRequest;
import com.casdore.car_mgmt.user.dto.response.UserResponse;
import com.casdore.car_mgmt.common.dto.response.ApiResponse;


public interface UserService {
    ApiResponse<UserResponse> createUser (CreateUserRequest userRequest);
}
