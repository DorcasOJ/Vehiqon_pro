package com.vehiqon.features.onboarding.repository;

import com.vehiqon.features.onboarding.entity.PasswordResetTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {
    Optional<PasswordResetTokenEntity> findByToken(String token);

    @Modifying
    @Transactional
    @Query("""
            UPDATE PasswordResetTokenEntity p
            SET p.used = true,
                p.usedAt = CURRENT_TIMESTAMP
            WHERE p.userId = :userId
                AND p.used = false
            """)
    void markAllAsUsedByUserId( UUID userId);

    @Query("""
            SELECT p
            FROM PasswordResetTokenEntity p
            WHERE p.token = :token
                AND p.used = false
                AND p.expiresAt > CURRENT_TIMESTAMP
            """)
    Optional<PasswordResetTokenEntity> findValidToken( String token);
}
