package com.vehiqon.features.onboarding.repository;

import com.vehiqon.common.enums.RoleEnum;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

//@Repository
//public interface UserRepository extends JpaRepository<UserEntity, UUID> {
//    boolean existsByEmail(String email);
//    boolean existsByPhoneNumber(String phoneNumber);
//    Optional<UserEntity> findByEmail(String email);
//    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
//
//
//}

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmailAndDeletedFalse(String email);
    boolean existsByPhoneNumberAndDeletedFalse(String phoneNumber);
    boolean existsByBvnAndDeletedFalse(String bvn);

    long countByIdInAndDeletedFalse(List<UUID> carIds);

    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    @Query("SELECT u.id FROM UserEntity u WHERE u.id IN :userIds AND u.deleted = false")
    List<UUID> findExistingIdsByIdIn(@Param("userIds") List<UUID> userIds);

    Optional<UserEntity> findByIdAndDeletedFalse(UUID userId);
    Optional<UserEntity> findByIdAndDeletedTrue(UUID userId);

    Optional<UserEntity> findByPhoneNumberAndDeletedFalse(String phoneNumber);

    Optional<UserEntity> findByBvnAndDeletedFalse(String bvn);

    List<UserEntity> findAllByStatusAndDeletedFalse(String status);

    List<UserEntity> findAllByIsVerifiedAndDeletedFalse(Boolean isVerified);


    @Query("""
    SELECT c
    FROM UserEntity c
    WHERE (:query IS NULL OR :query = '' OR (
            c.firstName ILIKE CONCAT('%', :query, '%') OR
            c.lastName ILIKE CONCAT('%', :query, '%') OR
            c.email ILIKE CONCAT('%', :query, '%') OR
            c.phoneNumber ILIKE CONCAT('%', :query, '%') OR
            c.status ILIKE CONCAT('%', :query, '%') OR
            c.gender ILIKE CONCAT('%', :query, '%')
            ))
        AND c.deleted = false
    """)
    Optional<Page<UserEntity>> searchUsersForAdmin(String query, Pageable pagable);


    @Modifying
    @Transactional
    @Query("""
            UPDATE UserEntity u
            SET u.isVerified = true
            WHERE u.id = :userId
                AND u.isVerified = false
                AND u.deleted = false
            """)
    void markUserAsIsVerified(UUID userId);

    @Modifying
    @Query("""
            UPDATE UserEntity u
            SET u.deleted = true,
            u.deletedAt = :deletedAt,
            u.deletedBy = :deletedBy
           WHERE u.id IN :ids
           AND u.deleted = false
           """)
    int softDeleteAllByIdIn(@Param("ids") List<UUID> ids,
                            @Param("deletedAt") LocalDateTime deletedAt,
                            @Param("deletedBy") UUID deletedBy);

    @Modifying
    @Query("""
            UPDATE UserEntity u
            SET u.deleted = false,
            u.deletedAt = null,
            u.deletedBy = null
           WHERE u.id IN :ids
           AND u.deleted = true
           """)
    int restoreAllByIdIn(@Param("ids") List<UUID> ids);

}
