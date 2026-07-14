package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarBrandRepository extends JpaRepository<BrandEntity, UUID> {
    Optional<BrandEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
   }
