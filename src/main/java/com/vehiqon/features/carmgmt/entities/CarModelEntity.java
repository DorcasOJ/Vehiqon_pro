package com.vehiqon.features.carmgmt.entities;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="models")
public class CarModelEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @Builder.Default
    @OneToMany(mappedBy = "model")
    private Set<CarEntity> cars = new HashSet<>();
}
