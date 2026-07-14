package com.vehiqon.common.entity;

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
@Table(name="subscription_plan")
public class SubscriptionPlan extends BaseEntity {

    private String name;
    private String description;
    private Integer price;
    private String currency;

    private String billingCycle ;
    private Integer max_cars;

    @Builder.Default
    private Boolean maintenanceReminders = Boolean.TRUE;
    @Builder.Default
    private Boolean prioritySupport = Boolean.FALSE;
    @Builder.Default
    private Boolean roadsideAssistance = Boolean.FALSE;
    @Builder.Default
    private Boolean analytics = Boolean.FALSE;
    @Builder.Default
    private Boolean active = Boolean.TRUE;

}
