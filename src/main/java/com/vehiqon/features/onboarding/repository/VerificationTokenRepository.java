package com.vehiqon.features.onboarding.repository;

import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, UUID> {


//    List<VerificationTokenEntity> findByUserAndTypeAndUsedFalse(
//            UserEntity user,
//            VerificationTokenTypeEnum type
//    );

    Optional<VerificationTokenEntity> findByToken(String token);

    Optional<VerificationTokenEntity> findByTokenAndType(
            String token,
            VerificationTokenTypeEnum type
    );

    List<VerificationTokenEntity> findByUserIdAndTypeAndUsedFalse(
            UUID userId,
            VerificationTokenTypeEnum type
    );

    List<VerificationTokenEntity> findByUserId(UUID userId);

    boolean existsByToken(String token);

    void deleteByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE VerificationTokenEntity t
            SET t.used = true,
                t.usedAt = :usedAt
            WHERE t.userId = :userId
                AND t.type = :type
                AND t.used = false
            """)
    int markAllAsUsed(UUID userId, VerificationTokenTypeEnum type, LocalDateTime usedAt);

    @Query("""
            SELECT t
            FROM VerificationTokenEntity t
            WHERE t.token = :token
                AND t.type = :type
                AND t.used = false
                AND t.expiresAt > CURRENT_TIMESTAMP
            """)
    Optional<VerificationTokenEntity> findActiveToken(String token,VerificationTokenTypeEnum type );

}
