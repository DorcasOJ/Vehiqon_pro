package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, UUID> {
    Optional<UserSessionEntity> findByUserIdAndDeviceIdAndLogoutAtIsNull(UUID userId, String deviceId);
    Optional<List<UserSessionEntity>> findByUserIdAndLogoutAtIsNull(UUID userId);
    boolean existsByIdAndLogoutAtIsNull(UUID id);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserSessionEntity s
            SET s.logoutAt = :logoutAt,
                s.active = false
            WHERE s.userId = :userId AND s.logoutAt IS NULL
            """)
    void logoutAllActiveSessions(@Param("userId") UUID userId,
                          @Param("logoutAt")LocalDateTime logoutAt);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserSessionEntity s
            SET s.logoutAt = :now,
                s.active = false
            WHERE s.logoutAt IS NULL
             AND s.lastActivityAt < :cutoffTime
            """)
    int expireInactiveSessions(@Param("cutoffTime") LocalDateTime cutoffTime,
                                 @Param("now") LocalDateTime now
                               );


//    s.durationSeconds = (EXTRACT(EPOCH FROM :now::local_date_time) - EXTRACT(EPOCH FROM s.loginAt::local_date_time))
//    s.durationSeconds = EXTRACT(EPOCH FROM (:now - s.loginAt))

}
