package com.vehiqon.features.insights.analytics.entities;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="feature_sessions")
public class FeatureSessionEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_session_id", nullable = false)
    private UUID userSessionId;

//    @Enumerated(EnumType.STRING)
//    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "feature")
    private FeatureEnum feature;

    private LocalDateTime startedTime;
    private LocalDateTime lastActivityTime;
    private LocalDateTime endedTime;
    @Builder.Default
    private Long durationSeconds = 0L;
}
