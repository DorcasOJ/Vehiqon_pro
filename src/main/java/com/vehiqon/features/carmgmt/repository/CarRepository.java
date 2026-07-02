package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends CrudRepository<CarEntity, UUID> {
    Optional<Boolean> existsByVin(String vin);
    Optional<Boolean> existsByPlateNumber(String plateNumber);
    Optional<List<CarEntity>> findByUserId(UUID userId);
    Optional<CarEntity> findByVin(String vin);
    Optional<CarEntity> findByPlateNumber(String plateNumber);
}
