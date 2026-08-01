package com.vehiqon.features.insights.analytics.entities.aggregation;

import com.vehiqon.features.carmgmt.enums.MaintenanceType;
import com.vehiqon.features.insights.analytics.enums.DayOfWeek;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import com.vehiqon.features.insights.analytics.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
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



//    private UUID favouriteCarId;
//    private UUID favouriteMechanicId;
//    private Integer preferredNotificationHour;
//    private NotificationChannel preferredNotificationChannel;
//    private Boolean prefersDarkMode;
//    private Boolean onboardingCompleted;
//    @Enumerated(EnumType.STRING)
//    @Column(length = 50)
//    private FeatureEnum favouriteFeature;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "feature_weights", columnDefinition = "jsonb")
    private Map<FeatureEnum, Double> featureWeights;

    private LocalTime preferredLoginTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DayOfWeek preferredLoginDay;

    @Enumerated(EnumType.STRING)
    @Column(length = 80)
    private MaintenanceType favouriteMaintenanceType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private PaymentMethod preferredPaymentMethod;

    private Integer averageSessionMinutes;
    private Boolean likesPushNotifications;
    private Boolean likesEmailNotifications;
    private Integer reminderLeadHours;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;

//    private Long averageSessionLength;
//    private Long riskScore;
//    private String preferredLanguage;
//    private String recommendedWidgets;
}

//Payments
//\
//Money the user spends on Vehiqon services, for example:
//Maintenance booking
//Subscription
// Inspection
//Parts purchase
//Towing
//Insurance (future)
