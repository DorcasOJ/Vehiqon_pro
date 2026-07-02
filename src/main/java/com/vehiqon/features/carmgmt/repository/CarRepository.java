package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID> {
    Boolean existsByVin(String vin);
    Boolean existsByPlateNumber(String plateNumber);
    List<CarEntity> findByUserId(UUID userId);
    Optional<CarEntity> findByVin(String vin);
    Optional<CarEntity> findByPlateNumber(String plateNumber);
}
