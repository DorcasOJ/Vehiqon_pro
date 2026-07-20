package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatisticsRepository extends JpaRepository<UserStatisticsEntity, UUID> {
    Optional<UserStatisticsEntity> findByUserId(UUID userId);
}
