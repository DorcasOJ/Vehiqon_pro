package com.vehiqon.car_mgmt.common.entity;

import com.vehiqon.car_mgmt.common.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="payments")
public class UserSubscription extends BaseEntity {

    private String startDate;
    private String endDate;
    private String renewalType;
    private String paymentReference;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id", nullable = false )
    private SubscriptionPlan subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false )
    private User user;

    @OneToOne(mappedBy = "userSubscription")
    private Payment payment;
}
