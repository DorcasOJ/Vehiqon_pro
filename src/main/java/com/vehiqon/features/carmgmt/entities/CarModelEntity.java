package com.vehiqon.features.carmgmt.entities;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "car_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarModelEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

//    private String year;

    @Column(name = "car_brand_id", nullable = false)
    private UUID carBrandId;
}