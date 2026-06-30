package com.casdore.car_mgmt.user.service;
import com.casdore.car_mgmt.user.dto.UserRequest;
import com.casdore.car_mgmt.user.dto.response.UserResponse;
import com.casdore.car_mgmt.user.dto.response.ApiResponse;


public interface UserService {
    ApiResponse<UserResponse> createUser (UserRequest userRequest);
}
