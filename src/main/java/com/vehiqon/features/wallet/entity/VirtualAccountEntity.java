package com.vehiqon.features.wallet.entity;

import com.vehiqon.common.entity.BaseEntity;
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
@Table(name="virtual_accounts")
@Entity
public class VirtualAccountEntity extends BaseEntity {
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_reference", nullable = false, unique = true)
    private String accountReference;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "bank_name")
    private String bankName;

    private String currency;

    @Column(name = "account_holder_id")
    private String accountHolderId;

    @Builder.Default
    private Boolean expired = false;
}
