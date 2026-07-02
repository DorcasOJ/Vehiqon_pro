package com.casdore.car_mgmt.carModel.repository;

import com.casdore.car_mgmt.common.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ModelRepository extends JpaRepository<Model, UUID> {
    List<Model> findByBrandId(UUID brandId);
}
