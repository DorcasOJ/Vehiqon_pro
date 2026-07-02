package com.vehiqon.features.carmgmt.entities;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="brands")
public class BrandEntity extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "brand")
    private Set<CarModelEntity> model;

    @OneToMany(mappedBy = "brand")
    private Set<CarEntity> cars;
}
