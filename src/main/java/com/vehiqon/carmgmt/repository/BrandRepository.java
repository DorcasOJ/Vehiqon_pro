package com.vehiqon.carmgmt.repository;

import com.vehiqon.carmgmt.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, UUID> {
    Optional<BrandEntity> findByNameIgnoreCase(String name);
}
