package com.vehiqon.features.email.mapper;

import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VerificationTokenMapper {

        public VerificationTokenEntity emailTokenToSave(UserEntity user, String token) {
           return VerificationTokenEntity.builder()
                            .token(token)
                            .type(
                                    VerificationTokenTypeEnum.EMAIL_VERIFICATION
                            )
                            .user(user)
                            .expiresAt(
                                    LocalDateTime.now().plusHours(24)
                            )
                            .build();
        }
    }
