package com.casdore.car_mgmt.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="service_history")
public class ServiceHistory extends BaseEntity {

    private String serviceType;
    private String description;
    private BigDecimal cost;
    private String serviceDate;
    private String nextServiceDate;
    private String mechanic;
    private String workshop;
    private String odometer;
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", nullable = false )
    private Car car;
}
