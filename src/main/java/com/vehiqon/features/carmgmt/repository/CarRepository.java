package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.carmgmt.enums.FuelType;
import com.vehiqon.features.carmgmt.enums.TransmissionEnum;
import com.vehiqon.features.onboarding.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID> {
    boolean existsByVin(String vin);
    boolean existsByPlateNumber(String plateNumber);
    boolean existsByEngineNumber(String engineNumber);

    Optional<CarDto.CarResponse> findByVin(String vin);
    Optional<CarDto.CarResponse> findByPlateNumber(String plateNumber);
//    void delete(UUID carId);

    Optional<List<CarEntity>> findAllByUserId(UUID userId);

    Optional<CarEntity> findByIdAndUserId(UUID carId, UUID userId);

    Optional<List<CarEntity>> findAllByCarBrandId(UUID brandId);

    Optional<List<CarEntity>> findAllByCarModelId(UUID modelId);

    Optional<List<CarEntity>> findAllByStatus(CarStatus status);

    Optional<List<CarEntity>> findAllByFuelType(FuelType fuelType);

    Optional<List<CarEntity>> findAllByTransmission(TransmissionEnum transmission);

    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse(
        c.id,
        c.nickname,
        c.vin,
        c.plateNumber,
        c.color,
        c.year,
        c.engineNumber,
        c.fuelType,
        c.transmission,
        c.odometer,
        c.purchaseDate,
        c.licenseExpiry,
        c.status,
        b.id,
        b.name,
        m.id,
        m.name
    )
    FROM CarEntity c
    JOIN BrandEntity b ON c.carBrandId = b.id
    JOIN CarModelEntity m ON c.carModelId = m.id
    WHERE c.userId = :userId
""")
    Optional<List<CarDetailsResponse>> findCarsDetailsByUserId(UUID userId);


    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse(
        c.id,
        c.nickname,
        c.vin,
        c.plateNumber,
        c.color,
        c.year,
        c.engineNumber,
        c.fuelType,
        c.transmission,
        c.odometer,
        c.purchaseDate,
        c.licenseExpiry,
        c.status,
        b.id,
        b.name,
        m.id,
        m.name
    )
    FROM CarEntity c, BrandEntity b, CarModelEntity m
    WHERE c.carBrandId = b.id
      AND c.carModelId = m.id
      AND c.id = :carId
      AND c.userId = :userId
    """)
    Optional<CarDetailsResponse> findCarDetails(UUID carId, UUID userId);


    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse(
        c.id,
        c.nickname,
        c.vin,
        c.plateNumber,
        c.color,
        c.year,
        c.engineNumber,
        c.fuelType,
        c.transmission,
        c.odometer,
        c.purchaseDate,
        c.licenseExpiry,
        c.status,
        b.id,
        b.name,
        m.id,
        m.name
    )
    FROM CarEntity c
    JOIN BrandEntity b ON c.carBrandId = b.id
    JOIN CarModelEntity m ON c.carModelId = m.id
    WHERE c.userId = :userId
        AND (:query IS NULL OR (
            c.nickname ILIKE CONCAT('%', :query, '%') OR
            c.vin ILIKE CONCAT('%', :query, '%') OR
            c.plateNumber ILIKE CONCAT('%', :query, '%')
            ))
        AND (:brandId IS NULL OR c.carBrandId = :brandId)
        AND (:status IS NULL OR c.status = :status)
""")
    Optional<Page<CarDetailsResponse>> searchCars(UUID userId, String query, UUID brandId, CarStatus status, Pageable pagable);

}


