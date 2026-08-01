package com.vehiqon.features.insights.analytics.entities.aggregation;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name="user_statistics")
public class UserStatisticsEntity extends BaseEntity {


    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Builder.Default
    private Long totalSessions = 0L;
    @Builder.Default
    private Long totalEvents = 0L;
    @Builder.Default
    private Long totalTimeSpent = 0L;
    @Builder.Default
    private Long totalMaintenanceCost =0L;
    private Long maintenanceCount;

    private Long totalPayments;
    private Long paymentCount;

    private Double activityScore;
//    40% Weekly Sessions + 20% Events + 20% Time Spent + 20% Maintenance Completion
    private Double engagementScore;
//    Opened Notifications + Reminder Completion + Feature Diversity + Searches + Dashboard Visits
    private Double vehicleHealthScore;
//    Upcoming overdue reminders + Maintenance completed + Vehicle age + Mileage + Inspection statu + No overdu
    private LocalDateTime lastActive;
//    private long monthlyActiveDays;


}
