package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ModelRepository extends JpaRepository<CarModelEntity, UUID> {
    List<CarModelEntity> findByCarBrandId(UUID brandId);
}
