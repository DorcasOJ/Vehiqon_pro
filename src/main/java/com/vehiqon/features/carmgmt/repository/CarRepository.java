package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.carmgmt.enums.FuelType;
import com.vehiqon.features.carmgmt.enums.TransmissionEnum;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID> {
    boolean existsByIdAndUserId(UUID id, UUID userId);

    boolean existsByVinAndDeletedFalse(String vin);
    boolean existsByPlateNumberAndDeletedFalse(String plateNumber);
    boolean existsByEngineNumberAndDeletedFalse(String engineNumber);
    boolean existsByIdAndDeletedFalse(@NonNull UUID carId);

    long countByIdInAndDeletedFalse(List<UUID> carIds);
    long countByIdInAndUserIdAndDeletedFalse(List<UUID> carId, UUID userId);

    Optional<CarDto.CarResponse> findByVinAndDeletedFalse(String vin);
    Optional<CarDto.CarResponse> findByPlateNumberAndDeletedFalse(String plateNumber);

    Optional<CarEntity> findByIdAndUserIdAndDeletedTrue(UUID carId, UUID userId);
    Optional<CarEntity> findByIdAndDeletedTrue(UUID carId);

    @Query("SELECT c.id FROM CarEntity c WHERE c.id IN :carIds AND c.deleted = false")
    List<UUID> findExistingIdsByIdIn(@Param("carIds") List<UUID> carIds);

    @Query("SELECT c.id FROM CarEntity c WHERE c.id IN :carIds AND c.userId = :userId AND c.deleted = false")
    List<UUID> findExistingIdsByInAndUserId(@Param("carIds") List<UUID> carIds,@Param("userId") UUID userId);

    Optional<List<CarEntity>> findAllByUserIdAndDeletedFalse(UUID userId);

    Optional<CarEntity> findByIdAndUserIdAndDeletedFalse(UUID carId, UUID userId);
    Optional<CarEntity> findByIdAndDeletedFalse(UUID carId);

    Optional<List<CarEntity>> findAllByCarBrandIdAndDeletedFalse(UUID brandId);

    Optional<List<CarEntity>> findAllByCarModelIdAndDeletedFalse(UUID modelId);

    Optional<List<CarEntity>> findAllByStatusAndDeletedFalse(CarStatus status);

    Optional<List<CarEntity>> findAllByFuelTypeAndDeletedFalse(FuelType fuelType);

    Optional<List<CarEntity>> findAllByTransmissionAndDeletedFalse(TransmissionEnum transmission);

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
    AND c.deleted = false
""")
    Optional<Page<CarDetailsResponse>> findCarsDetailsByUserId(UUID userId, Pageable pageable);


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
      AND c.deleted = false
    """)
    Optional<CarDetailsResponse> findCarDetailsByUser(UUID carId, UUID userId);


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
      AND c.deleted = false
    """)
    Optional<CarDetailsResponse> findCarDetailsById(UUID carId);

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
      AND c.deleted = false
    """)
    Optional<Page<CarDetailsResponse>> findAllCarDetails(Pageable pageable);



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
    WHERE (:query IS NULL OR (
            c.nickname ILIKE CONCAT('%', :query, '%') OR
            c.vin ILIKE CONCAT('%', :query, '%') OR
            c.plateNumber ILIKE CONCAT('%', :query, '%')
            ))
        AND (:brandId IS NULL OR c.carBrandId = :brandId)
        AND (:status IS NULL OR c.status = :status)
        AND c.deleted = false
""")
    Optional<Page<CarDetailsResponse>> searchCarsForAdmin(String query, UUID brandId, 
                                                          CarStatus status, Pageable pagable);

//    @Modifying
//    void deleteByIdAndUserId(UUID id, UUID userId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE CarEntity c
            SET c.deleted = true,
            c.deletedAt = :deleted_at,
            c.deletedBy = :deletedBY
           WHERE c.id IN :ids
           AND c.userId = :userId
           AND c.deleted = false
           """)
    int softDeleteByIdInAndUserId(@Param("ids") List<UUID> ids,
                                  @Param("userId") UUID userId,
                                  @Param("deletedAt") LocalDateTime deletedAt,
                                  @Param("deletedBy") UUID deletedBy);

    @Modifying
    @Transactional
    @Query("""
            UPDATE CarEntity c
            SET c.deleted = true,
            c.deletedAt = :deleted_at,
            c.deletedBy = :deletedBY
           WHERE c.id IN :ids
           AND c.deleted = false
           """)
    int softDeleteAllByIdIn(@Param("ids") List<UUID> ids,
                            @Param("deletedAt") LocalDateTime deletedAt,
                            @Param("deletedBy") UUID deletedBy);

    @Modifying
    @Transactional
    @Query("""
            UPDATE CarEntity c
            SET c.deleted = false,
            c.deletedAt = null,
            c.deletedBy = null
           WHERE c.id IN :ids
           AND c.deleted = true
           """)
    int restoreAllByIdIn(@Param("ids") List<UUID> ids);

    @Query("""
            SELECT
                COUNT(*) AS totalCars,
                COUNT(CASE WHEN deleted = false AND status = ACTIVE THEN 1 END) AS activeCars,
                COUNT(CASE WHEN deleted = true THEN 1 END) AS deletedCars
            FROM CarEntity c
            WHERE c.userId = :userId
            """)
    CarDto.CarStatisticsResponse getCarStatistics(@Param("userId") UUID userId);

    Optional<List<CarEntity>> findByUserIdAndDeletedTrue(UUID userId);
}


