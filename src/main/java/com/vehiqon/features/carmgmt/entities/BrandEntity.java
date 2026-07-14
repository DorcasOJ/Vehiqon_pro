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
@Table(name="car_brands")
public class BrandEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

//    @Builder.Default
//    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
//    private Set<CarModelEntity> models = new HashSet<>();
//
//    @Builder.Default
//    @OneToMany(mappedBy = "brand")
//    private Set<CarEntity> cars = new HashSet<>();
}
