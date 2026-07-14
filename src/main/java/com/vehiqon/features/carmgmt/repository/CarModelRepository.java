package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarModelRepository extends JpaRepository<CarModelEntity, UUID> {

    List<CarModelEntity> findAllByCarBrandId(UUID brandId);

    Optional<CarModelEntity> findByNameIgnoreCaseAndCarBrandId(
            String name,
            UUID brandId
    );

    boolean existsByNameIgnoreCaseAndCarBrandId(
            String name,
            UUID brandId
    );

//    List<CarModelEntity> findAllByYear(String year);

}
