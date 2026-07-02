package com.vehiqon.features.onboarding.mapper;

import com.vehiqon.features.onboarding.dto.LoginResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginResponseMapper {

    @Value("${JWT_EXPIRATION}")
    private Long jwtExpiration;

    private final UserMapper userMapper;

    public LoginResponse toResponse(String token, UserEntity user) {
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .user(userMapper.toResponse(user))
                .build();
    }
//        return CarResponse.builder()
//                .id(car.getId())
//                .nickname(car.getNickname())
//                .plateNumber(car.getPlateNumber())
//                .brand(car.getBrand().getName())
//                .model(car.getModel().getName())
//                .fuelType(car.getFuelType().name())
//                .transmission(car.getTransmission().name())
//                .vin(car.getVin())
//                .build();
//    }
}
