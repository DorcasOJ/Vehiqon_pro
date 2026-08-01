package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.UserEventEntity;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import org.hibernate.type.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEventRepository extends JpaRepository<UserEventEntity, UUID> {

    Optional<List<UserEventEntity>> findByUserIdAndFeatureAndEventTypeAndOccurredAtBetween(
            UUID userId, FeatureEnum feature, EventType entityType, LocalDateTime startDate, LocalDateTime endDate);

}
