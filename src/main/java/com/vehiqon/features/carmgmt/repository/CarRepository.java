package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID> {
    boolean existsByVin(String vin);
    boolean existsByPlateNumber(String plateNumber);
    boolean existsByEngineNumber(String engineNumber);

    Optional<List<CarEntity>> findAllByUser(UserEntity user);
    Optional<CarEntity> findByIdAndUser(UUID carId, UserEntity user);
    Optional<CarDto.CarResponse> findByVin(String vin);
    Optional<CarDto.CarResponse> findByPlateNumber(String plateNumber);
//    void delete(UUID carId);
}
