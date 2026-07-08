package com.vehiqon.features.wallet.entity;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name="virtual_accounts")
@Entity
public class VirtualAccountEntity extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false, unique = true)
    private String accountReference;

    @Column(nullable = false)
    private String accountName;

//    private String bankCode;

    private String bankName;

    private String currency;

    private String accountHolderId;

    @Builder.Default
    private Boolean active = true;
}
