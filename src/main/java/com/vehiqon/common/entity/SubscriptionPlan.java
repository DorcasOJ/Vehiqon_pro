package com.vehiqon.common.entity;

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
@Table(name="subscription_plan")
public class SubscriptionPlan extends BaseEntity{

    private String startDate;
    private String endDate;
    private String renewalType;
    private String paymentReference;
    private String status; // paid/completed

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false )
    private SubscriptionPlan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false )
    private User user;

    @OneToOne(mappedBy = "subscriptionPlan")
    private UserSubscription userSubscription;
}
