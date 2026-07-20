package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.aggregation.UserFeatureStatisticsEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserPersonalisationEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserFeatureStatisticsRepository extends JpaRepository<UserFeatureStatisticsEntity, UUID> {
    Optional<UserFeatureStatisticsEntity> findByUserId(UUID userId);
}
