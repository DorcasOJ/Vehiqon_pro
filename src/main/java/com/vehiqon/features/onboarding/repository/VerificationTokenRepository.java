package com.vehiqon.features.onboarding.repository;

import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, UUID> {
    Optional<VerificationTokenEntity> findByToken(String token);

    Optional<VerificationTokenEntity> findByTokenAndType(
            String token,
            VerificationTokenTypeEnum type
    );

    List<VerificationTokenEntity> findByUserAndTypeAndUsedFalse(
            UserEntity user,
            VerificationTokenTypeEnum type
    );
}
