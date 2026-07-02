package com.vehiqon.car_mgmt.auth.dto;

import com.vehiqon.car_mgmt.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType; //bearer
    private Long expiresIn; // 3600
    private UserResponse user;

}
