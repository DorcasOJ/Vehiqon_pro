package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.entities.UserEventEntity;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, UUID> {
    Optional<UserSessionEntity> findFirstByUserIdAndLogoutTimeIsNull(UUID userId);
    List<UserSessionEntity> findByUserIdAndLogoutTimeIsNull(UUID userId);
    boolean existsByUserIdAndLogoutTimeIsNull(UUID userId);

}
