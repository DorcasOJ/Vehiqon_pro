package com.vehiqon.features.insights.analytics.entities.aggregation;

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
@Table(name="user_feature_statistics")
public class UserFeatureStatisticsEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FeatureEnum feature;

    @Builder.Default
    @Column(nullable = false)
    private Long visitCount = 0L;
    /**
     * Number of successful/completed actions within this feature.
     * e.g. Reminder Completed, Payment Successful, Car Added.
     */
    @Builder.Default
    @Column(nullable = false)
    private Long totalEvents = 0L;

    private LocalDateTime firstVisitedAt;
    private LocalDateTime lastVisitedAt;

    /**
     * Total time spent inside this feature.
     */
    @Builder.Default
    private Long totalDurationSeconds = 0L; // time spent // time spent

}
