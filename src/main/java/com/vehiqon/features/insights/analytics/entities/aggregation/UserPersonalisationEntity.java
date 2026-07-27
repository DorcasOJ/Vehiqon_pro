package com.vehiqon.features.insights.analytics.entities.aggregation;

import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import com.vehiqon.features.insights.Notification.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_personalisation")
public class UserPersonalisationEntity {
    @Id
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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;

//    private Long averageSessionLength;
//    private Long riskScore;
////    private String preferredLanguage;
//    private String recommendedWidgets;
}
