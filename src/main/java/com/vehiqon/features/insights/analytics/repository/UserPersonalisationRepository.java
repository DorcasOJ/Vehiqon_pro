package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.aggregation.UserPersonalisationEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPersonalisationRepository extends JpaRepository<UserPersonalisationEntity, UUID> {
    Optional<UserPersonalisationEntity> findByUserId(UUID userId);
}
