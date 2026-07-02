package com.vehiqon.car_mgmt.car.repository;

import com.vehiqon.car_mgmt.common.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    Boolean existsByVin(String vin);
    Boolean existsByPlateNumber(String plateNumber);
    List<Car> findByUserId(UUID userId);
    Optional<Car> findByVin(String vin);
    Optional<Car> findByPlateNumber(String plateNumber);
}
