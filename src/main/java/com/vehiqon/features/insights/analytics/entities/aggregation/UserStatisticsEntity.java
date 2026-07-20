package com.vehiqon.features.insights.analytics.entities.aggregation;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class UserStatisticsEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;
    private Long totalSessions;
    private Long totalEvents;
    private Long totalTimeSpent;
//    private Long lifetimeSpending;
//    private Long averageMaintenanceCost;
    private Long totalMaintenanceCost;
    private Long maintenanceCount;

    private Long totalPayments;
    private Long paymentCount;

//    private Long completionRate;
    private Double activityScore;
//    40% Weekly Sessions + 20% Events + 20% Time Spent + 20% Maintenance Completion
    private Double engagementScore;
//    Opened Notifications + Reminder Completion + Feature Diversity + Searches + Dashboard Visits
    private Double vehicleHealthScore;
//    Upcoming overdue reminders + Maintenance completed + Vehicle age + Mileage + Inspection statu + No overdu
    private LocalDateTime lastActive;
//    private long monthlyActiveDays;
}
