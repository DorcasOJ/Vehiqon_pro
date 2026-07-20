package com.vehiqon.features.insights.analytics.entities.aggregation;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import com.vehiqon.features.insights.analytics.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_personalisation")
public class UserPersonalisationEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private FeatureEnum favouriteFeature;

    private UUID favouriteCarId;
    private UUID favouriteMechanicId;
    private Integer preferredNotificationHour;
    private NotificationChannel preferredNotificationChannel;
    private Boolean prefersDarkMode;
    private Boolean onboardingCompleted;

//    private Long averageSessionLength;
//    private Long riskScore;
////    private String preferredLanguage;
//    private String recommendedWidgets;
}
