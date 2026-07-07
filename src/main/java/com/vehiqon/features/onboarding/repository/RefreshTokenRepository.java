package com.vehiqon.features.onboarding.repository;

import com.vehiqon.features.onboarding.entity.RefreshTokenEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<List<RefreshTokenEntity>> findAllByUser(UserEntity user);

    Optional<List<RefreshTokenEntity>> findAllByUserAndRevokedFalse(UserEntity user);

    Optional<RefreshTokenEntity> findByUser(UserEntity user );
    @Modifying
    @Transactional
    @Query("""
    update RefreshTokenEntity t
    set t.revoked = true,
        t.expired = true,
        t.revokedAt = CURRENT_TIMESTAMP
    where t.user = :user
      and t.revoked = false
      AND t.expired = false
""")
    void revokeAll(@Param("user") UserEntity user);
}
