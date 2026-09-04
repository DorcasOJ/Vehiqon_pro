package com.vehiqon.security.session.repository;

import com.vehiqon.security.session.entity.RefreshTokenEntity;
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

    Optional<RefreshTokenEntity> findByTokenHash(String token); // refresh
    Optional<List<RefreshTokenEntity>> findAllByUserId(UUID userId); // all
    Optional<List<RefreshTokenEntity>> findAllByUserIdAndRevokedFalse(UUID userId);
    Optional<RefreshTokenEntity> findByUserIdAndRevokedFalse(UUID userId);

//    Optional<RefreshTokenEntity> findFirstByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE RefreshTokenEntity t
        SET t.revoked = true,
            t.expired = true,
            t.revokedAt = CURRENT_TIMESTAMP
        WHERE t.userId = :userId
          AND t.revoked = false
          AND t.expired = false
    """)
    void revokeAll(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE RefreshTokenEntity t
        SET t.revoked = true,
            t.expired = true,
            t.revokedAt = CURRENT_TIMESTAMP
        WHERE t.tokenHash = :token
          AND t.revoked = false
          AND t.expired = false
    """)
    void revokeToken(@Param("token") String token);
    }

