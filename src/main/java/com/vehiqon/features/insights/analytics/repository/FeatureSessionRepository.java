package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeatureSessionRepository extends JpaRepository<FeatureSessionEntity, UUID> {

    Optional<FeatureSessionEntity> findFirstByUserIdAndEndedTimeIsNull(UUID userId);
    Optional<FeatureSessionEntity> findByUserSessionIdAndFeatureNameAndEndedTimeIsNull(UUID userSessionId, FeatureEnum feature);
    List<FeatureSessionEntity> findByUserIdAndEndedTimeIsNull(UUID userId);
    boolean existsByUserIdAndEndedTimeIsNull(UUID userId);
}
