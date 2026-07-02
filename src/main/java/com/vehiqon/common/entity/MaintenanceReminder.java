package com.vehiqon.common.entity;

import com.vehiqon.common.enums.ReminderStatus;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="maintenance_reminders")
public class MaintenanceReminder extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity carEntity;

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    @Builder.Default
    private Boolean notificationSent = false;
}
