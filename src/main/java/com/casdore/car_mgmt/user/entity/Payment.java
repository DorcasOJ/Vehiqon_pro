package com.casdore.car_mgmt.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="payments")
public class Payment extends BaseEntity{
    private String reference;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String provider;
    private LocalDateTime paymentDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_subscription_id", nullable = false )
    private UserSubscription userSubscription;
}
