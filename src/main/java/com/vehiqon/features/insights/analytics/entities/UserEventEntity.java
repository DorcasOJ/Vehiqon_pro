package com.vehiqon.features.insights.analytics.entities;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_events")
public class UserEventEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_session_id", nullable = false)
    private UUID userSessionId;

    @Column(name = "feature_session_id", nullable = false)
    private UUID featureSessionId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private FeatureEnum feature;

    @Enumerated(EnumType.STRING)
    private EntityEnum entityType;

    private UUID entityId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object > metadata;

    private LocalDateTime occurredAt;
}
