package com.vehiqon.common.entity;

import com.vehiqon.common.enums.SubscriptionStatus;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_subscriptions")
public class UserSubscription extends BaseEntity {

//    private String startDate;
    private String expiryDate;
    private String renewalType;
    private String paymentReference;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Builder.Default
    private Boolean autoRenew = Boolean.TRUE;

    @Column(name = "subscription_plan_id", nullable = false)
    private UUID subscriptionPlanId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

}
