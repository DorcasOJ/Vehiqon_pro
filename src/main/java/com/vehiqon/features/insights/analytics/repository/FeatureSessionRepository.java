package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeatureSessionRepository extends JpaRepository<FeatureSessionEntity, UUID> {

    Optional<FeatureSessionEntity> findFirstByUserIdAndEndedTimeIsNull(UUID userId);
    Optional<FeatureSessionEntity> findByUserSessionIdAndFeatureAndEndedTimeIsNull(UUID userSessionId, FeatureEnum feature);
    List<FeatureSessionEntity> findByUserIdAndEndedTimeIsNull(UUID userId);
    boolean existsByUserIdAndEndedTimeIsNull(UUID userId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE FeatureSessionEntity s
            SET s.endedTime = s.lastActivityTime
            WHERE s.endedTime IS NULL
             AND s.lastActivityTime < :cutoffTIme
            """)
    int expireInactiveFeatureSessions(@Param("cutoffTIme") LocalDateTime cutoffTIme);


    Optional<FeatureSessionEntity> findByUserSessionIdAndEndedTimeIsNull(UUID userSessionId);


}
//s.durationSeconds = (EXTRACT(EPOCH FROM :now) - EXTRACT(EPOCH FROM s.startedTime))
