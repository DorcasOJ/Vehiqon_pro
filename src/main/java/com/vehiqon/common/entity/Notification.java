package com.vehiqon.common.entity;

import com.vehiqon.features.onboarding.entity.UserEntity;
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
@Table(name="notifications")
public class Notification extends BaseEntity {
    private String title;
    private String message;
    private String type;
    private String status;
    private String sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false )
    private UserEntity user;
}
