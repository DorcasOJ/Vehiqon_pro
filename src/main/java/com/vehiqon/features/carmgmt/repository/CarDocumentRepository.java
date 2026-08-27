package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.CarDocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarDocumentRepository extends JpaRepository<CarDocumentEntity, UUID> {
    Page<CarDocumentEntity> findAllByDeletedFalse(Pageable pageable);
    Optional<CarDocumentEntity> findByIdAndDeletedFalse(UUID documentId);
    Page<CarDocumentEntity> findAllByCarIdAndDeletedFalse(UUID carId, Pageable pageable);
    Optional<CarDocumentEntity> findByIdAndCarIdAndDeletedFalse(UUID id, UUID carId);
    List<CarDocumentEntity> findAllByIdInAndCarIdAndDeletedFalse(List<UUID> ids, UUID carId);
   @Query(value = """
        SELECT d.* FROM car_documents d
        JOIN cars c ON d.car_id = c.id
        WHERE d.id = :id 
          AND c.id = :carId 
          AND c.user_id = :userId 
          AND d.deleted = true
        """, nativeQuery = true)
    Optional<CarDocumentEntity> findSoftDeletedByIdAndCarId(
            @Param("id") UUID id,
            @Param("carId") UUID carId
    );

    @Modifying
    @Query(value = """
        UPDATE car_documents d
        SET deleted = false, deleted_at = NULL, deleted_by = NULL
        FROM cars c
        WHERE d.car_id = c.id
          AND d.id = :id
          AND c.id = :carId
          AND d.deleted = true
        """, nativeQuery = true)
    int restoreDocument(@Param("id") UUID id, @Param("carId") UUID carId);

    @Modifying
    @Query(value = """
        UPDATE car_documents d
        SET deleted = false, deleted_at = NULL, deleted_by = NULL
        FROM cars c
        WHERE d.car_id = c.id
          AND d.id IN :ids
          AND c.id = :carId
          AND d.deleted = true
        """, nativeQuery = true)
    int restoreDocuments(@Param("ids") List<UUID> ids, @Param("carId") UUID carId);


    @Query("""
            SELECT cd
            FROM CarDocumentEntity cd
            JOIN CarEntity c ON cd.carId = c.id
            WHERE c.userId = :userId
            AND cd.deleted = false
            """)
    Page<CarDocumentEntity> findDocumentsByUserId(UUID userId, Pageable pageable);

    Optional<List<CarDocumentEntity>> findByCarIdAndDeletedTrue(UUID carId);
}
