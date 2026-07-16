package com.vehiqon.features.onboarding.mapper;

import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.features.onboarding.entity.PasswordResetTokenEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    @Value("${JWT_EXPIRATION}")
    private Long jwtExpiration;

    private final UserMapper userMapper;

    public LoginResponse toLoginResponse(String accessToken, String refreshToken, UserEntity user) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .user(userMapper.toResponse(user))
                .build();
    }

    public PasswordResetTokenEntity toPasswordResetTokenEntity(UserEntity userId, String token) {
        return PasswordResetTokenEntity.builder()
                .token(token)
                .userId(userId.getId())
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .used(false)
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
