package com.casdore.car_mgmt.user.service;
import com.casdore.car_mgmt.common.dto.UserRequest;
import com.casdore.car_mgmt.common.dto.response.UserResponse;
import com.casdore.car_mgmt.common.dto.response.ApiResponse;


public interface UserService {
    ApiResponse<UserResponse> createUser (UserRequest userRequest);
}
