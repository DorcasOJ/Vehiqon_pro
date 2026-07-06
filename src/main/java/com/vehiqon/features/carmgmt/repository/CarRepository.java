package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID> {
    Optional<Boolean> existsByVin(String vin);
    Optional<Boolean> existsByPlateNumber(String plateNumber);
    Optional<Boolean> existsByEngineNumber(String engineNumber);

    Optional<List<CarDto.CarResponse>> findByUserId(UUID userId);
    Optional<CarDto.CarResponse> findByVin(String vin);
    Optional<CarDto.CarResponse> findByPlateNumber(String plateNumber);
}
